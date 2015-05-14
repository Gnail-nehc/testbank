Ext.application({
	views: [
    	'Main',
    	'ConfigurationGrid',
    	'CheckpointGrid',
    	'LogGrid',
    	'RtpInfoGrid'
    ],
    models: [
         'Config',
         'Checkpoint',
         'Log',
         'RtpInfo'
    ],
    stores: [
         'Config',
         'Checkpoint',
         'Log',
         'RtpInfo'
    ],
    autoCreateViewport: true,
    name: 'testbank'
});
Ext.Loader.setConfig({
    enabled: true
});