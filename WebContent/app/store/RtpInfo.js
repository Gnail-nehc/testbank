Ext.define('testbank.store.RtpInfo', {
    extend: 'Ext.data.Store',

    requires: [
        'testbank.model.RtpInfo'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'testbank.model.RtpInfo',
            pageSize : 30,
            storeId: 'RtpInfo',
            proxy: {
                type: 'ajax',
                afterRequest: function(request, success) {
                    if(!success){
                        Ext.Msg.alert('错误','RtpInfo请求失败');
                        return;
                    }
                },
                api: {
                    read: 'task/getRtpInfos',
                    destroy: 'task/deleteRtpInfo'
                },
                extraParams: {
                    collectionid: '',
                    phoneno: '',
                    cardno: '',
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