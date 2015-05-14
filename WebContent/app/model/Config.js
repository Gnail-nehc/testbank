Ext.define('testbank.model.Config', {
    extend: 'Ext.data.Model',
    requires: ['Ext.data.UuidGenerator'],
	idgen: 'uuid',
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
            name: 'responsecode',
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
        },
        {
        	name: 'comment',
            type: 'string'
        }
    ],

    validations: [
        {
            type: 'length',
            field: 'collectionid',
            min: 1
        },
        {
            type: 'length',
            field: 'requesttype',
            min: 1
        },
        {
            type: 'length',
            field: 'responsecode',
            min: 1
        }      
    ]
});