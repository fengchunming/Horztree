(function($){
	// Using it without an object
	$.printPage = function(url, options) { return $.fn.printPage(url, options); };
	
	$.fn.printPage = function(url, options) {
		var settings = {
			url : false,
			message : "打印准备中，请稍候..."
		};
		
		if(options)
			{ $.extend(settings, options); }
		
		if(url) 
			settings.url = url;
		
		var PrintPage = {
			loadPrintDocument : function(el, settings) {
				$.sticky(settings.message);
				this.addIframeToPage(el, settings);
			},
			iframe : function(url) {
				return '<iframe id="printPage" name="printPage" src='
						+ url
						+ ' style="position:absolute;top:0px; left:0px;width:0px; height:0px;border:0px;overfow:none; z-index:-1"></iframe>';
			},
			addIframeToPage : function(el, settings) {
				if (!$("#printPage")[0]) {
					$(el).append(this.iframe(settings.url));
					$('#printPage').on("load", function() {
						PrintPage.printit();
					});
				} else {
					$('#printPage').attr("src", settings.url);
				}
			},
			printit : function() {
				window.frames["printPage"].focus();
				window.frames["printPage"].print();
			}
		};
//		if (typeof(LODOP) !="undefined" && LODOP) {
//			LODOP.ADD_PRINT_URL(0,0,"100%","100%", settings.url);
//			$.get("rest/sys/printType/" + settings.type , function(data){
//				if(data.page == "X")
//					LODOP.SET_PRINT_PAGESIZE(data.orient, data.width * 10, data.height * 10, data.page);
//				else
//					LODOP.SET_PRINT_PAGESIZE(data.orient, 0, 0, data.page);
//
//				for(var i = 0; i< LODOP.GET_PRINTER_COUNT(); i++){
//					if(LODOP.GET_PRINTER_NAME(i) == data.printer){
//						LODOP.SET_PRINTER_INDEX(i);
//						break;
//					}
//				}
//				//LODOP.PRINT();
//				LODOP.PREVIEW();
//			});
//		}else{
			PrintPage.loadPrintDocument('body', settings);
//		}
	};
})(jQuery);