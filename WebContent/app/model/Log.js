Ext.define('testbank.model.Log', {
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
            name: 'verifiedfield',
            type: 'string'
        },
        {
            name: 'type',
            type: 'string'
        },
        {
            name: 'expectedtext',
            type: 'string'
        },
        {
            name: 'time',
            type: 'string'
        },
        {
            name: 'result',
            type: 'string'
        },
        {
            name: 'actualvalue',
            type: 'string'
        }
        
    ]
});