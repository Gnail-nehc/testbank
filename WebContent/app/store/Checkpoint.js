Ext.define('testbank.store.Checkpoint', {
    extend: 'Ext.data.Store',

    requires: [
        'testbank.model.Checkpoint'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'testbank.model.Checkpoint',
            pageSize : 30,
            storeId: 'Checkpoint',
            proxy: {
                type: 'ajax',
                afterRequest: function(request, success) {
                    if(!success){
                        Ext.Msg.alert('错误','Checkpoint请求失败');
                        return;
                    }
                },
                api: {
                    create: 'task/addCheckpoint',
                    read: 'task/getAllCheckpoints',
                    update: 'task/addCheckpoint',
                    destroy: 'task/deleteCheckpoint'
                },
                extraParams: {
                    configid: ''
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