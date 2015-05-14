Ext.define('testbank.view.LogGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.LogGrid',
    id: 'LogGrid',
    flex:8,
	region: 'center',
    title: '测试结果列表',
    autoFill : true,
    store: 'Log',
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
		    		id:'SearchedCollectionId',
		    		fieldLabel : "机构渠道",
		    		labelWidth: 60
		    	},
		    	{
		    		xtype:'datefield',
		    		id:'SearchedDate',
		    		fieldLabel : "测试日期",
		    		editable:true,
		    		labelWidth: 60,
		    		format:"Ymd"
		    		
		    	},
		    	{
		    		xtype: 'button',
		            handler: function(button, event) {
		            	var collectionid=Ext.getCmp('SearchedCollectionId').getValue();
		            	var date=Ext.getCmp('SearchedDate').rawValue;
		            	Ext.getStore('Log').proxy.extraParams.collectionid=collectionid;
		            	Ext.getStore('Log').proxy.extraParams.date=date;
		            	Ext.getStore('Log').load();
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
					flex:3,
				    dataIndex: 'verifiedfield',
				    text: '验证字段',
				},
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'type',
				    text: '判断条件',
				    flex: 3
				},
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'expectedtext',
				    text: '预期值',
				    flex: 4,
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
					xtype: 'gridcolumn',
				    dataIndex: 'result',
				    text: '结果',
				    flex: 2,
				    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        if(value=='t' || value=='T' || value=='true'){
                            return '<span style="color: #006000;font-size:14px">' + "成功" + '</span>';
                        }else if(value=='f' || value=='F' || value=='false'){
                            return '<span style="color: #CE0000;font-size:14px">' + "失败" + '</span>';
                        }
                    }
				},
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'actualvalue',
				    text: '字段实际值',
				    flex: 4,
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
		                                Ext.getStore('Log').removeAt(rowIndex);
		                                Ext.getStore('Log').sync({
		                                    success:function(){
		                                    	Ext.getStore('Log').load();
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
				store : 'Log',
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
			})
        });
        me.callParent(arguments);
    }
});