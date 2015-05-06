$(function(){
	jQuery.barcode = {
		timerId : 0,
		el : undefined,
		lengthMin : 5,
		cache : '',
		listener : {},
		listen: function(opt) {
			if(!opt.name) return;
			$.barcode.listener[opt.name] = {};
			$.barcode.listener[opt.name].regx = opt.regx?opt.regx:null;
			$.barcode.listener[opt.name].filter = opt.filter?opt.filter:true;
			$.barcode.listener[opt.name].callback = opt.callback?opt.callback:function(){};
		},
		stop:function(param) {
			;
		},
		callback :function(){
			if($.barcode.cache.length >= $.barcode.lengthMin){
				$.each($.barcode.listener,function(){
					if(this.filter && (!this.regx || this.regx.test($.barcode.cache))){
						this.callback($.barcode.cache);
					}
				});
			}
			$.barcode.cache = '';
		}
	};
	
	if(typeof _barcodeInited == 'undefined'){
		$(document).keypress(function(e) {
			if($.barcode.listener == {}) return;
			if($.barcode.cache == ''){
				$.barcode.timerId = window.setTimeout($.barcode.callback, 200);
			}
			switch (e.which) {
			case 13:
				window.clearTimeout($.barcode.timerId);
				$.barcode.callback();
				break;
			default:
				$.barcode.cache += String.fromCharCode(e.which);
			}
		});
		_barcodeInited = true;
	}
});
