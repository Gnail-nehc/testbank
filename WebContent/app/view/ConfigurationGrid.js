var collectionIdStore=new Ext.data.Store({
	autoLoad:false,
	proxy:{
		type:'ajax',
		getMethod: function(){ return 'POST'; },
		url:'task/getCollectionIds',
        reader: {
            type: 'json',
            messageProperty: 'msg',
            root: 'obj'
        },
        writer: {
            type: 'json',
            allowSingle: false
        },
        afterRequest: function(request, success) {
            if(!success){
                Ext.Msg.alert('错误','collectionIdStore请求失败');
                return;
            }
        }
	},
	fields:['id','text']
});

var requestTypeStore=new Ext.data.Store({
	autoLoad:false,
	proxy:{
		type:'ajax',
		getMethod: function(){ return 'POST'; },
		url:'task/getSupportedRequestTypes',
        extraParams: {
        	bankKey : ''
        },
        reader: {
            type: 'json',
            messageProperty: 'msg',
            root: 'obj'
        },
        afterRequest: function(request, success) {
            if(!success){
                Ext.Msg.alert('错误','requestTypeStore请求失败');
                return;
            }
        }
	},
	fields:['id','text']
});

var responseCodeStore=new Ext.data.Store({
	autoLoad:false,
	proxy:{
		type:'ajax',
		getMethod: function(){ return 'POST'; },
		url:'task/getSupportedResponseCodes',
        extraParams: {
        	bankKey : '',
        	requesttypeKey : ''
        },
        reader: {
            type: 'json',
            messageProperty: 'msg',
            root: 'obj'
        },
        afterRequest: function(request, success) {
            if(!success){
                Ext.Msg.alert('错误','responseCodeStore请求失败');
                return;
            }
        }
	},
	fields:['id','text']
});

Ext.define('testbank.view.ConfigurationGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.ConfigurationGrid',
    id: 'ConfigurationGrid',
    flex:10,
	region: 'west',
    title: '配置列表',
    autoFill : true,
    store: 'Config',
    stripeRows : true,
    margins:'60,0,60,60',
    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
        	dockedItems: [
		    {
		        xtype: 'toolbar',
		        dock: 'top',
		        items: [
		        {
		            xtype: 'button',
		            handler: function(button, event) {
                        var store = Ext.getStore('Config');
                        store.insert(0,{});
                        var rowEditing = Ext.getCmp('ConfigurationGrid').getPlugin("RowEditPlugin");
                        rowEditing.startEdit(0,1); 
                    },
		            icon: 'image/add.png',
		            tooltip: '新增配置'
		        },
		        {
		            xtype: 'tbseparator'
		        },
		        {
		            xtype: 'button',
		            handler: function(button, event) {
		                Ext.getStore('Config').load();
		            },
		            icon: 'image/refresh.png',
		            tooltip: '刷新'
		        },
		        {
		            xtype: 'tbseparator'
		        },
		        {
		        	xtype: 'button',
		            handler: function(button, event) {
		            	Ext.MessageBox.alert({
		            		title:"注意事项",
		            		msg:"配置注意事项<br>" +
		            				"1、机构卡号不得重复。手机号为支付页面填写的手机号码，若无需填写可以不配置，反之必须配置；<br>" +
		            				"2、Mock银行配置时可以添加一到若干条检查点，用来验证上游系统发给RTP的字段是否符合预期。可以不配置直接模拟返回，则字段验证日志中无验证记录；<br>" +
		            				"3、若需要补充其它机构渠道，或某机构的其他业务种类，以确保此类机构渠道和业务类型实现稳定为前提，可向 liang.chen 提出补充配置请求。<br><br>" +
		            			"测试注意事项<br>" +
		            				"Mock配置没有生效的分析检查步骤如下：<br>" +
		            				"1、确保在测试环境或者UAT环境正确部署并开启了相关银行队列，队列处理程序工作正常；<br>" +
		            				"2、确保需Mock的机构渠道的相关业务是可测的，当前跨境支付业务暂不支持Mock；<br>" +
		            				"3、向RTP开发 zbcai 询问某订单的RTPforMock日志,配置url格式为<br>" +
		            				"http://192.168.81.33/testbank/task/receive/collectionid=xx/requesttype=xx/cardno=xx/phoneno=xx/<br>" +
		            				"测试中填写的卡号手机号（如果有的话）通过队列发送给RTP处理，此url的collectionid,requesttype,cardno,phoneno值必须与Mock配置一致方能生效。否则将按原有处理逻辑返回。",
		            	});
		            },
		            icon: 'image/helper.png',
		            tooltip: '帮助'
		        }]
		    }],
		    plugins: [
	            Ext.create('Ext.grid.plugin.RowEditing', {
	                pluginId: 'RowEditPlugin',
	                listeners: {
	                    edit: {
	                        fn: me.onRowEditingEdit,
	                        scope: me
	                    }
	                }
	            })
            ],
		    columns: [
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'id',
				    hidden:true
				},
				{
				    xtype: 'gridcolumn',
					flex:3,
				    text: '机构渠道',
				    dataIndex: 'collectionid',
				    editor: {
                        xtype: 'combobox',
                        editable:false,
                        allowBlank:false,
                        id:'CollectionIdCombo',
                        store: collectionIdStore,
                        displayField:'text',                                
    					valueField:'id',
                        listeners: {
                        	change : {
    							fn: me.getSupportedRequestTypes,
    							scope: me
    						}
                        }
                    }
				},
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'requesttype',
				    text: '业务种类',
				    editor: {
                        xtype: 'combobox',
                        editable:false,
                        allowBlank:false,
                        id:'RequestTypeCombo',
                        store: requestTypeStore,
                        displayField:'text',                                
    					valueField:'id',
                        listeners: {
                        	change : {
    							fn: me.getSupportedResponseCodes,
    							scope: me
    						}
                        }
                    },
				    flex: 3
				},
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'responsecode',
				    text: '返回码',
				    editor: {
                        xtype: 'combobox',
                        editable:false,
                        allowBlank:false,
                        id:'ResponseCodeCombo',
                        displayField:'text',                                
    					valueField:'id',
                        store: responseCodeStore,
                    },
				    flex: 4,
				},
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'cardno',
				    text: '机构卡号',
				    editor: {
                        xtype: 'textfield',
                        editable:true,
                        allowBlank:false,
                        id:'CardNoText',
                        regex:/^[0-9]+$/,
				    	regexText:'必须是数字',
                    },
				    flex: 4
				},
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'phoneno',
				    text: '手机号码',
				    editor: {
                        xtype: 'textfield',
                        editable:true,
                        id:'PhoneNoText',
                        regex:/^[0-9]+$/,
				    	regexText:'必须是数字',
                    },
				    flex: 4
				},
				{
					xtype: 'gridcolumn',
				    dataIndex: 'comment',
				    text: '备注',
				    editor: {
                        xtype: 'textfield',
                        editable:true,
                        id:'CommentText',
                    },
				    flex: 10
				},
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'time',
				    text: '创建/修改时间',
				    flex: 4,
				    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
				    	if(value!=""){
				    		var time=value.split(" ")[1];
				    		value=value.split(" ")[0]+' '+time.substring(0,2)+':'+time.substring(2,4)+':'+time.substring(4,6);
				    	}
				    	return value;
				    }
				},
			    {
			        xtype: 'actioncolumn',
			        text: '删除',
			        flex:1,
			        items: [
			        {
			            handler: function(view, rowIndex, colIndex, item, e, record, row) {
			            	Ext.MessageBox.confirm(
				                "confirm",
				                "确认删除？",
				                function(e){
				                    if(e=='yes'){
				                    	Ext.getStore('Config').removeAt(rowIndex);
		                                Ext.getStore('Config').sync({
		                                    success:function(){
		                                    	Ext.getStore('Checkpoint').removeAll();
		                                    	Ext.getStore('Config').load();
		                                    }
		                                });
				                    }
				                }
				            ); 
			            },
			            icon: 'image/delete.png',
			            tooltip: 'delete'
			        }]
			    }
			],
			bbar : new Ext.PagingToolbar({
				store : 'Config',
				displayInfo : true,
				beforePageText:"第",
				afterPageText:"/ {0}页",
				firstText:"首页",
				prevText:"上一页",
				nextText:"下一页",
				lastText:"尾页",
				refreshText:"刷新",
				displayMsg : "当前显示记录从 {0} - {1} 总 {2} 条记录",
				emptyMsg : "没有相关记录!",
				listeners : {
					change : function(that,pageData,eOpts){
						if(pageData!=null){
							var data=Ext.Array.slice( this.store.data.items, pageData.fromRecord-1, pageData.toRecord);
							this.store.loadData(data);
						}
					}
				}
			}),
		    listeners: {
		    	itemmousedown : {
		            fn: me.itemmousedown,
		            scope: me
		        },
		        itemmouseenter: {
                    fn: me.itemmouseenter,
                    scope: me
                }
		    }
        });
        me.callParent(arguments);
    },
	getSupportedRequestTypes : function( that, newValue, oldValue, eOpts ){
		requestTypeStore.proxy.extraParams.bankKey=newValue;
		requestTypeStore.load();
	},
	getSupportedResponseCodes : function( that, newValue, oldValue, eOpts ){
		responseCodeStore.proxy.extraParams.bankKey=Ext.getCmp('CollectionIdCombo').getValue();
		responseCodeStore.proxy.extraParams.requesttypeKey=newValue;
		responseCodeStore.load();
	},
	onRowEditingEdit: function(editor, context, eOpts) {
		Ext.getStore('Config').proxy.extraParams.rawcollectionid=Ext.getCmp('CollectionIdCombo').rawValue;
		Ext.getStore('Config').proxy.extraParams.rawrequesttype=Ext.getCmp('RequestTypeCombo').rawValue;
		Ext.getStore('Config').proxy.extraParams.rawresponsecode=Ext.getCmp('ResponseCodeCombo').rawValue;
        Ext.getStore('Config').sync({
            success:function(){
            	Ext.getStore('Config').load();
            }
        });
    },
    itemmousedown : function( that, record, item, index, e, eOpts ){
    	Ext.getCmp('AddCheckpointButton').disabled=false;
    	Ext.getCmp('LoadCheckpointButton').disabled=false;
    	Ext.getStore('VerifiedFieldStore').proxy.extraParams.collectionid=record.raw.collectionid;
    	Ext.getCmp('Main').SelectedConfigId=record.raw.id;
    	Ext.getStore('Checkpoint').proxy.extraParams.configid=record.raw.id;
    	Ext.getStore('Checkpoint').load();
	},
	itemmouseenter: function( that, record, item, index, e, eOpts ){
		Ext.Ajax.request( {
			url : 'task/getConfigDescription',
			method: "POST",
			params : {  
				id : record.data.id,
			},
		    success : function(response, options) {
		    	var object=JSON.parse(response.responseText);
		    	if(object.success){
		    		Ext.create('Ext.tip.ToolTip', {
		                width: 120,
		                height:80,
		        		target:item,
		        		html: object.obj
		                //dismissDelay: 15000         //15秒后自动隐藏
		            });
		    	}else
		    		Ext.Msg.alert("错误","获取描述失败");
		    },
		    failure: function(response, opts) {
		    	lm.hide();
             	Ext.Msg.alert("错误","getConfigDescription请求错误");
            }
		});
    }
});