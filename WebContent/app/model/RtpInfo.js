Ext.define('testbank.model.RtpInfo', {
    extend: 'Ext.data.Model',
//    requires: ['Ext.data.UuidGenerator'],
//	idgen: 'uuid',
    fields: [
        {
            name: 'id',
            type: 'string'
        },
        {
            name: 'collectionid',
            type: 'string'
        },
        {
            name: 'requesttype',
            type: 'string'
        },
        {
            name: 'cardno',
            type: 'string'
        },
        {
            name: 'phoneno',
            type: 'string'
        },
        {
            name: 'time',
            type: 'string'
        }
    ]
});