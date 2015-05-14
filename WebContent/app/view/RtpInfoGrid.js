Ext.define('testbank.view.RtpInfoGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.RtpInfoGrid',
    id: 'RtpInfoGrid',
    flex:8,
	region: 'center',
    title: 'RTPMock日志列表',
    autoFill : true,
    store: 'RtpInfo',
    stripeRows : true,
    margins:'60,60,60,60',
    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
        	dockedItems: [
		    {
		    	xtype:'toolbar',
		    	dock:'top',
		    	items:[
		    	{
		    		xtype:'textfield',
		    		id:'InfoSearchedCollectionId',
		    		fieldLabel : "机构渠道",
		    		labelWidth: 60
		    	},
		    	{
		    		xtype:'textfield',
		    		id:'InfoSearchedCardNo',
		    		fieldLabel : "机构卡号",
		    		labelWidth: 60
		    	},
		    	{
		    		xtype:'textfield',
		    		id:'InfoSearchedPhoneNo',
		    		fieldLabel : "手机号码",
		    		labelWidth: 60
		    	},
		    	{
		    		xtype:'datefield',
		    		id:'InfoSearchedDate',
		    		fieldLabel : "测试日期",
		    		editable:true,
		    		labelWidth: 60,
		    		format:"Ymd"
		    	},
		    	{
		    		xtype: 'button',
		            handler: function(button, event) {
		            	var collectionid=Ext.getCmp('InfoSearchedCollectionId').getValue();
		            	var cardno=Ext.getCmp('InfoSearchedCardNo').getValue();
		            	var phoneno=Ext.getCmp('InfoSearchedPhoneNo').getValue();
		            	var date=Ext.getCmp('InfoSearchedDate').rawValue;
		            	Ext.getStore('RtpInfo').proxy.extraParams.collectionid=collectionid;
		            	Ext.getStore('RtpInfo').proxy.extraParams.phoneno=phoneno;
		            	Ext.getStore('RtpInfo').proxy.extraParams.cardno=cardno;
		            	Ext.getStore('RtpInfo').proxy.extraParams.date=date;
		            	Ext.getStore('RtpInfo').load();
		            },
		            icon: 'image/find.png',
		            tooltip: '查找'
		    	}]
		    }],
		    columns: [
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'id',
				    hidden:true
				},
				{
				    xtype: 'gridcolumn',
					flex:3,
				    dataIndex: 'collectionid',
				    text: '机构渠道',
				},
				{
				    xtype: 'gridcolumn',
					flex:3,
				    dataIndex: 'requesttype',
				    text: '业务种类',
				},
				{
				    xtype: 'gridcolumn',
					flex:5,
				    dataIndex: 'cardno',
				    text: '机构卡号',
				},
				{
				    xtype: 'gridcolumn',
					flex:5,
				    dataIndex: 'phoneno',
				    text: '手机号码',
				},
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'time',
				    text: '测试时间',
				    flex: 3,
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
		                                Ext.getStore('RtpInfo').removeAt(rowIndex);
		                                Ext.getStore('RtpInfo').sync({
		                                    success:function(){
		                                    	Ext.getStore('RtpInfo').load();
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
				store : 'RtpInfo',
				displayInfo : true,
				beforePageText:"第",
				afterPageText:"/ {0}页",
				firstText:"首页",
				prevText:"上一页",
				nextText:"下一页",
				lastText:"尾页",
				refreshText:"刷新",
				displayMsg : "总 {2} 条记录",
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
		        itemdblclick : {
		        	fn: me.itemdblclick,
		            scope: me
		        }
		    }
        });
        me.callParent(arguments);
    },
    itemdblclick : function( that, record, item, index, e, eOpts ){
		var lm = new Ext.LoadMask(Ext.getCmp('RtpInfoGrid'), { 
			msg : '读取中。。。', 
			removeMask : true
		}); 
		lm.show();
 		Ext.Ajax.request( {
			url : 'task/getRtpInfoDetail',
			method: "POST",
			params : {
				id : record.raw.id
			},
		    success : function(response, options) {
		    	lm.hide();
		    	var detail=JSON.parse(response.responseText);
		    	var win = Ext.create("Ext.Window", {
		    	    title: "log detail",
		    	    modal: true,
		    	    width: 360,
		    	    height: 180,
		    	    autoShow: false,
		    	    items:[
						{
							width: 360,
				    	    height: 180,
							xtype:'textarea',
							id:"Text2Checkpoints",
							autoScroll:true,
							value:detail
						}
					]
		    	});
		    	win.show();
		    },
		    failure: function(response, opts) {
		    	lm.hide();
             	Ext.Msg.alert("错误","getRtpInfoDetail请求失败");
            }
		});
	}
});