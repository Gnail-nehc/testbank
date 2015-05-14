Ext.define('testbank.store.Config', {
    extend: 'Ext.data.Store',

    requires: [
        'testbank.model.Config'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'testbank.model.Config',
            pageSize : 30,
            storeId: 'Config',
            proxy: {
                type: 'ajax',
                afterRequest: function(request, success) {
                    if(!success){
                        Ext.Msg.alert('错误','Config请求失败');
                        return;
                    }else{
                    	if(request.action=='create'){
                    		var obj=request.proxy.reader.rawData;
                    		if(!obj.success){
                    			Ext.Msg.alert("错误",obj.msg,function(){
                    				Ext.getStore('Config').load();
                    			});
                    		}
                    	}
                    }
                },
                api: {
                    create: 'task/addConfig',
                    read: 'task/getAllConfig',
                    update: 'task/updateConfig',
                    destroy: 'task/deleteConfig'
                },
		        actionMethods: {
		        	create : 'POST',
		            update : 'POST',
		        },
                extraParams: {
                    rawcollectionid: '',
                    rawrequesttype: '',
                    rawresponsecode: ''
                },
                reader: {
                    type: 'json',
                    messageProperty: 'msg',
                    root: 'rows'
                },
                writer: {
                    type: 'json',
                    allowSingle: false
                }
            },
            sorters: {
                direction: 'DESC',
                property: 'time'
            }
        }, cfg)]);
    }
});