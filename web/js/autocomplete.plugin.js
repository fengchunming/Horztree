(function($) {
	// Extend jQuery
	$.fn.autocompletePlugin = function(opts, ts){
		
		// Initialize options with default values
		opts = jQuery.extend({
			what: undefined
		},opts||{});
		
		if(opts.what == 'goods')
			$(this).autocomplete({
				  appendTo : "body",
			      source: function( request, response ) {
				      $.ajax({
				          url: 'rest/mm/goodsList',
				          dataType: "json",
				          data: { _LM: 100,_SH: request.term},
				          success: function( data ) {
				             response( $.map( data.list, function( item ) {
				                 return {
				                   label: item.goodsCode + " - " + item.goodsName,
				                   value: item.goodsCode,
				                   object : item
				                 };
				             }));
				          }
				     });
			      },
			      select: function( event, ui ) {
			    	 	var goods = ui.item.object;
			   			goods.goodsUnit = {};
			   			goods.goodsUnit.uomCode = goods.standardUnit.uomCode;
			   			goods.lotAttr = {};
			   			ts.getModel().set(goods);
			      }
			});
		
		
		if(opts.what == 'location')
			$(this).autocomplete({
				  appendTo : "body",
			      source: function( request, response ) {
				      $.ajax({
				          url: 'rest/wm/locationList',
				          dataType: "json",
				          data: { _LM: 100,_SH: request.term},
				          success: function( data ) {
				             response( $.map( data.list, function( item ) {
				                 return {
				                   label: item.barcode + " - " + item.sectionCode,
				                   value: item.barcode,
				                   object : item
				                 };
				             }));
				          }
				     });
			      }
			});
	
	};
})(jQuery);