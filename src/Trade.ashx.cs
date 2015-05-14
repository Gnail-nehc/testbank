using System;
using System.Linq;
using System.Reflection;
using System.Web;
using System.Web.Configuration;
using System.Xml.Linq;
using Newtonsoft.Json;
using Payment.Switch.CreditCard.Core;
using Payment.Switch.CreditCard.Entity;
using Payment.Switch.Utils;
using Payment.Switch.AllCreditCard.WcfService.RTPMock;
using System.Text;
using Payment.Switch.RTPLogging;
using System.Collections.Generic;
using Payment.Switch.AllCreditCard.WcfService.RTPMock.Data;
using System.Threading;

namespace Payment.Switch.AllCreditCard.WcfService
{
    /// <summary>
    /// 银行交易处理
    /// </summary>
    public class Trade : IHttpHandler
    {
        public void ProcessRequest(HttpContext context)
        {
            context.Response.Cache.SetCacheability(HttpCacheability.NoCache);
            context.Response.Cache.SetNoStore();
            context.Response.Cache.SetNoServerCaching();
            context.Response.Clear();
            context.Response.Write(JsonConvert.SerializeObject(Execute(context), new JsonSerializerSettings { NullValueHandling = NullValueHandling.Ignore, DefaultValueHandling = DefaultValueHandling.Ignore, Converters = new System.Collections.Generic.List<JsonConverter> { new Newtonsoft.Json.Converters.IsoDateTimeConverter { DateTimeFormat = "yyyyMMddHHmmss" } } }));
            context.Response.Flush();
            context.ApplicationInstance.CompleteRequest();
        }

        /// <summary>
        /// 请求前置，返回结果
        /// </summary>
        /// <param name="context"></param>
        /// <returns></returns>
        private static AtomResponse Execute(HttpContext context)
        {
            //获取请求实体
            string requestType = System.IO.Path.GetFileName(context.Request.Path);
            AtomRequest request = AtomRequestFactory.Create(context, requestType);
            if (request == null)
            {
                return new AtomResponse
                {
                    TransStatus = TransStatus.CHECKFAILURE,
                    DealRemark = string.Format("交易类型{0}错误", requestType)
                };
            }

            //验证付款方式
            if (string.IsNullOrWhiteSpace(request.CollectionId))
            {
                return new AtomResponse
                {
                    TransStatus = TransStatus.CHECKFAILURE,
                    DealRemark = "付款方式不能为空"
                };
            }
            string assemblyName;
            try
            {
                assemblyName = GetExchangeAssembleyName(request.CollectionId);
            }
            catch (Exception e)
            {
                return new AtomResponse
                {
                    CollectionId = request.CollectionId,
                    TransStatus = TransStatus.CHECKFAILURE,
                    DealRemark = string.Concat("读取银行处理程序集异常，", e.Message)
                };
            }
            if (string.IsNullOrWhiteSpace(assemblyName))
            {
                return new AtomResponse
                {
                    CollectionId = request.CollectionId,
                    TransStatus = TransStatus.CHECKFAILURE,
                    DealRemark = "读取的银行处理程序集为空"
                };
            }

            //解密卡信息
            try
            {
                if (!string.IsNullOrWhiteSpace(request.CardNo)) request.CardNo = SecurityHelper.DesDecrypt(request.CardNo, WebConfigurationManager.AppSettings["CardKey"]);
                if (!string.IsNullOrWhiteSpace(request.ExpiryDate)) request.ExpiryDate = SecurityHelper.DesDecrypt(request.ExpiryDate, WebConfigurationManager.AppSettings["CardKey"]);
                if (!string.IsNullOrWhiteSpace(request.CardVerifyNo)) request.CardVerifyNo = SecurityHelper.DesDecrypt(request.CardVerifyNo, WebConfigurationManager.AppSettings["CardKey"]);
                if (!string.IsNullOrWhiteSpace(request.BindId)) request.BindId = SecurityHelper.DesDecrypt(request.BindId, WebConfigurationManager.AppSettings["CardKey"]);
            }
            catch (Exception e)
            {
                return new AtomResponse
                {
                    CollectionId = request.CollectionId,
                    TransStatus = TransStatus.CHECKFAILURE,
                    DealRemark = string.Concat("解密卡信息异常，", e.Message)
                };
            }

            //get mocked RTP config data
            try
            {
                string collectionid = !string.IsNullOrWhiteSpace(request.CollectionId) ? request.CollectionId : "";
                string cardno = !string.IsNullOrWhiteSpace(request.CardNo) ? request.CardNo : "";
                string phoneno = !string.IsNullOrWhiteSpace(request.PhoneNumber) ? request.PhoneNumber : "";
                string url = WebConfigurationManager.AppSettings["MockServiceUrl"] + "/collectionid=" + collectionid + "/requesttype=" + requestType + "/cardno=" + cardno + "/phoneno=" + phoneno + "/";
                HttpWebResponseUtility.encoding = Encoding.UTF8;
                string content = HttpWebResponseUtility.GetResponseText(url, 20000, "", null).Replace("\"", "");
                CLogHelper.WriteLog("MOCK DEBUG INFO", url+" "+content, null);
                //LogManager.GetLogger(typeof(Trade)).Info("MOCK DEBUG INFO",content,DateTime.Now);
                //LogManager.GetLogger(request.CollectionId).Info(string.Format("content[{0}],url[{1}]", content, url));
                //LogManager.Info(request.CollectionId, string.Format("content[{0}],url[{1}]", content, url));

                if (!String.IsNullOrEmpty(content))
                {
                    string[] values = content.Split(new string[] { "@" }, StringSplitOptions.RemoveEmptyEntries);
                    string fakeCollectionId = values[0];
                    string fakeRequestType = values[1];
                    string fakeResCode = values[2];
                    string fakeCardNo = values[3];
                    content = content.Replace(fakeCollectionId + "@" + fakeRequestType + "@" + fakeResCode + "@" + fakeCardNo + "@", "");

                    string fakePhoneNo = (string.IsNullOrEmpty(content) || content.StartsWith("@")) ? "" : values[4];
                    fakeResCode = fakeResCode == "0" ? "00" : fakeResCode;
                    if (content.Contains("~"))
                    {
                        content = content.Substring(content.IndexOf("@") + 1);
                        IList<TestLog> list = Verification.GetTestLogs(content, request);
                        string info = "";
                        foreach (var log in list)
                        {
                            info += log.VerifiedField + "@" + log.Type + "@" + log.ExpectedText + "@" + log.TestTime + "@" + (log.Result ? "t@" : "f@") + log.ActualValue + ";";
                        }
                        info = info.Substring(0, info.Length - 1);
                        //post request to MockServer
                        IDictionary<string, string> parameters = new Dictionary<string, string>();
                        parameters.Add("collectionid", fakeCollectionId);
                        parameters.Add("requesttype", fakeRequestType);
                        parameters.Add("cardno", fakeCardNo);
                        parameters.Add("phoneno", fakePhoneNo);
                        parameters.Add("verifiedinfo", info);
                        HttpWebResponseUtility.CreatePostHttpResponse(WebConfigurationManager.AppSettings["VerificationLogServiceUrl"], parameters, 20000, null, Encoding.UTF8, null);
                    }
                    //construct AtomResponse
                    return new AtomResponse
                    {
                        CollectionId = request.CollectionId,
                        TransStatus = fakeResCode == "00" ? TransStatus.SUCCEED : TransStatus.FAILURE,
                        BankRespCode = fakeResCode,
                        BankRefNo = "9999"
                    };
                }
                //else if ("UPOP.BIND.CC".Equals(request.CollectionId, StringComparison.CurrentCultureIgnoreCase) ||
                //    "UPOP.BIND.DC".Equals(request.CollectionId, StringComparison.CurrentCultureIgnoreCase))
                //{
                //    return new AtomResponse
                //    {
                //        CollectionId = request.CollectionId,
                //        TransStatus = TransStatus.FAILURE,
                //        BankRespCode = "01",
                //        DealRemark = "协议支付Mock配置无效",
                //        BankRespMsg = "协议支付Mock配置无效"
                //    };
                //}
                new Thread(() => WriteRtpLog(collectionid, requestType, cardno, phoneno, url + " " + content)).Start();
            }
            catch (Exception ex)
            {
                return new AtomResponse
                {
                    CollectionId = request.CollectionId,
                    TransStatus = TransStatus.EXCEPTION,
                    DealRemark = ex.Message,
                    BankRespExtension = ex.StackTrace,
                    BankRespCode = "XX"
                };
            }

            //执行交易
            try
            {
                dynamic instance = Assembly.Load(assemblyName).CreateInstance(string.Concat(assemblyName, ".", request.RequestType));
                if (instance == null)
                {
                    return new AtomResponse
                    {
                        CollectionId = request.CollectionId,
                        TransStatus = TransStatus.EXCEPTION,
                        DealRemark = string.Format("实例化[{0}.{1}]异常，返回值为NULL", assemblyName, request.RequestType)
                    };
                }
                return instance.Execute(request);
            }
            catch (Exception e)
            {
                return new AtomResponse
                {
                    CollectionId = request.CollectionId,
                    TransStatus = TransStatus.EXCEPTION,
                    DealRemark = string.Format("请求[{0}.{1}]异常，{2}", assemblyName, request.RequestType, e.Message)
                };
            }
        }

        /// <summary>
        /// 根据银行的标识代码（如：icbc.cc）来获取处理请求的程序集名称
        /// </summary>
        /// <returns>处理请求的程序集名称</returns>
        private static string GetExchangeAssembleyName(string collectionId)
        {
            var result = BankMappingConfig.XElementNew.Elements("bank").Single(x => x.Attribute("code").Value.Equals(collectionId, StringComparison.OrdinalIgnoreCase));
            return ((XElement)result.FirstNode).Value;
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }

        private static void WriteRtpLog(string collectionid,string requesttype,string cardno,string phoneno,string detail)
        {
            IDictionary<string, string> parameters = new Dictionary<string, string>();
            parameters.Add("collectionid", collectionid);
            parameters.Add("requesttype", requesttype);
            parameters.Add("cardno", cardno);
            parameters.Add("phoneno", phoneno);
            parameters.Add("detail", detail);
            HttpWebResponseUtility.CreatePostHttpResponse(WebConfigurationManager.AppSettings["RtpLogServiceUrl"], parameters, 20000, null, Encoding.UTF8, null);
        }

    }
}