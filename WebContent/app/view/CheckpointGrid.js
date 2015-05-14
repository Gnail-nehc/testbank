var verifiedFieldStore = Ext.create('Ext.data.Store',{
	storeId:"VerifiedFieldStore",
	autoLoad:false,
	proxy:{
		type:'ajax',
		getMethod: function(){ return 'POST'; },
		url:'task/getVerifiedFieldsByCollectionId',
		extraParams: {
        	collectionid : ''
        },
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
                Ext.Msg.alert('错误','verifiedFieldStore请求失败');
                return;
            }
        }
	},
	fields:['id','text']
});

Ext.define('testbank.view.CheckpointGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.CheckpointGrid',
    id: 'CheckpointGrid',
    flex:8,
	region: 'east',
    title: '检查点列表',
    autoFill : true,
    store: 'Checkpoint',
    stripeRows : true,
    margins:'60,60,60,0',
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
                        var store = Ext.getStore('Checkpoint');
                        store.insert(0,{});
                        var rowEditing = Ext.getCmp('CheckpointGrid').getPlugin("RowEditingPlugin");
                        rowEditing.startEdit(0,1); 
                    },
                    id:'AddCheckpointButton',
		            icon: 'image/add.png',
		            tooltip: '新增检查点'
		        },
		        {
		            xtype: 'tbseparator'
		        },
		        {
		            xtype: 'button',
		            handler: function(button, event) {
		                Ext.getStore('Checkpoint').load();
		            },
		            icon: 'image/refresh.png',
		            id:'LoadCheckpointButton',
		            tooltip: '刷新'
		        }]
		    }],
		    plugins: [
	            Ext.create('Ext.grid.plugin.RowEditing', {
	                pluginId: 'RowEditingPlugin',
	                listeners: {
	                    edit: {
	                        fn: me.onRowEdit,
	                        scope: me
	                    }
	                }
	            })
            ],
		    columns: [
				{
				    xtype: 'gridcolumn',
					flex:3,
				    dataIndex: 'verifiedfield',
				    text: '验证字段',
				    editor: {
                        xtype: 'combobox',
                        displayField:'text',                                
    					valueField:'id',
                        editable:false,
                        allowBlank:false,
                        id:'VerifiedFieldCombo',
                        store: verifiedFieldStore,
                    }
				},
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'type',
				    text: '判断条件',
				    editor: {
                        xtype: 'combobox',
                        editable:false,
                        allowBlank:false,
                        id:'CompareTypeCombo',
                        store: ['RegExpress','Equal','Contains','Length','DateFormat',
                                'IsEmpty','IsIDCard','IsMobilePhoneNumber','IsChinese'],
                        listeners: {
				        	select : {
					    		fn: me.selecttype,
					    		scope: me
					    	}
                        }
                    },
				    flex: 4
				},
				{
				    xtype: 'gridcolumn',
				    dataIndex: 'expectedtext',
				    id:'ExpectedColumn',
				    text: '预期值',
				    editor: {
                        xtype: 'textfield',
                        editable:true,
                        allowBlank:false,
                        id:'ExpectedText'
                    },
				    flex: 4,
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
				                    	Ext.getStore('Checkpoint').removeAt(rowIndex);
		                                Ext.getStore('Checkpoint').proxy.extraParams.configid=Ext.getCmp('Main').SelectedConfigId;
		                                Ext.getStore('Checkpoint').sync({
		                                    success:function(){
		                                    	Ext.getStore('Checkpoint').load();
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
				store : 'Checkpoint',
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
			})
        });
        me.callParent(arguments);
    },
	onRowEdit: function(editor, context, eOpts) {
        Ext.getStore('Checkpoint').sync({
            success:function(){
            	Ext.getStore('Checkpoint').load();
            }
        });
    },
    selecttype : function( combo, records, eOpts ){
    	if(records==null)
    		return ;
    	var selection=records[0].raw[0];
    	var editor={};
    	if('RegExpressEqualContains'.indexOf(selection)!=-1){
    		editor = {
          			xtype: 'textfield',
          			editable:true,
          			allowBlank:false,
          			id:'ExpectedText',
          			regex:/^((?![\/:*?"<>|@']).)*$/,
			    	regexText:"禁止包含字符\/:*?\"<>|@'",
          	    };
    	} else if ('IsEmptyIsIDCardIsMobilePhoneNumberIsChinese'.indexOf(selection)>-1) {
	    	editor = {
      			xtype: 'combo',
      			editable:false,
      			allowBlank:false,
      			id:'ExpectedText',
      			store: ['true','false']
      	    };
	    } else if ('DateFormat'==selection) {
	    	editor = {
      			xtype: 'combo',
      			editable:false,
      			allowBlank:false,
      			id:'ExpectedText',
      			store: ['yyyyMMdd','yyyy-MM-dd','yyyy/M/d','yyyy-MM-ddTHH:mm:ss','yyyy/M/d H:mm:ss','yyyy年M月d日 H:mm:ss','yyyy年M月d日','H:mm:ss','H:mm','M月d日','yyyy年M月','yyyy/M/d H:mm']
      	    };
	    } else if ('Length'==selection) {
	    	editor = {
	  			xtype: 'numberfield',
	  			editable:true,
	  			allowBlank:false,
	  			id:'ExpectedText',
	  			maxValue: 100,
		        minValue: 1
	  	    };
	    }
    	Ext.getCmp('CheckpointGrid').columns[2].setEditor(editor);
	}
});