Ext.define('testbank.store.Log', {
    extend: 'Ext.data.Store',

    requires: [
        'testbank.model.Log'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'testbank.model.Log',
            pageSize : 30,
            storeId: 'Log',
            proxy: {
                type: 'ajax',
                afterRequest: function(request, success) {
                    if(!success){
                        Ext.Msg.alert('错误','Log请求失败');
                        return;
                    }
                },
                api: {
                    read: 'task/getTestLogs',
                    destroy: 'task/deleteLog'
                },
                extraParams: {
                    collectionid: '',
                    date: '',
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