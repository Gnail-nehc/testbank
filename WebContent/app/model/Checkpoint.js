Ext.define('testbank.model.Checkpoint', {
    extend: 'Ext.data.Model',
    requires: ['Ext.data.UuidGenerator'],
	idgen: 'uuid',
    fields: [
        {
            name: 'id',
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
        }
    ],

    validations: [
        {
            type: 'length',
            field: 'verifiedfield',
            min: 1
        },
        {
            type: 'length',
            field: 'expectedtext',
            min: 1
        },
        {
            type: 'length',
            field: 'type',
            min: 1
        }      
    ]
});