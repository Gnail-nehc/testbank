Ext.define('testbank.view.Main', {
    extend: 'Ext.container.Viewport',
    id: 'Main',
    layout:'border',
    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
        	items:[
        	{
        		xtype: 'panel',
        		flex:2,
        		bodyStyle:"background-image:url('image/head.jpg')",
        		region: 'north'
        	},
        	{
        		xtype:'tabpanel',
        		region: 'center',
				flex:32,
			    activeTab:0,
			    listeners: {
			        tabchange : {
			            fn: me.tabchange,
			            scope: me
			        }
			    },
			    items:[
			    {
			    	title:"模拟银行配置入口",
					bodyStyle:"background-image:url('image/background.jpg')",
					id:"ConfigTab",
					layout: 'border',
					items:[
				    	Ext.widget('ConfigurationGrid'),
				    	//Ext.widget('CheckpointGrid')
					]
			    },
//			    {
//			    	title:"银行字段验证日志",
//					bodyStyle:"background-image:url('image/background.jpg')",
//					id:"LogTab",
//					layout: 'border',
//					items:[
//				    	Ext.widget('LogGrid'),
//					]
//			    },
			    {
			    	title:"RTP Log",
					bodyStyle:"background-image:url('image/background.jpg')",
					id:"RtpInfoTab",
					layout: 'border',
					items:[
				    	Ext.widget('RtpInfoGrid'),
					]
			    }]
        	},
        	{
        		xtype: 'panel',
				html : '<a color="blue" href="mailto:gnail_nehc@aliyun.com">Contact Author</a><font color="blue"> |  Copyright ©2014 Liang Chen | powered by ExtJS 4.2</font>',
				bodyStyle:"background-image:url('image/foot.jpg')",
				region: 'south',
				flex:1
        	}],
        	listeners: {
        		afterrender : {
	        		fn: me.afterrender,
	        		scope: me
	        	}
        	}
        });
        me.callParent(arguments);
    },
    afterrender : function(){
//    	Ext.getCmp('AddCheckpointButton').disabled=true;
//    	Ext.getCmp('LoadCheckpointButton').disabled=true;
    	Ext.getStore('Config').load();
	},
	tabchange : function( tabPanel, newCard, oldCard, eOpts ){
		var tabName = newCard.title;
		switch(tabName)
		{
    		case "银行字段验证日志":
				Ext.getStore('Log').load();
    			break;
    		case "RTP Log":
				Ext.getStore('RtpInfo').load();
    			break;
    		case "模拟银行配置入口":
//    			Ext.getCmp('AddCheckpointButton').disabled=true;
//    	    	Ext.getCmp('LoadCheckpointButton').disabled=true;
    	    	Ext.getStore('Config').load();
    			break;
		}
	}
});
