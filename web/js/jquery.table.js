(function($) {
	// Extend jQuery
	$.fn.resizetable = function(opts) {
		opts = jQuery.extend({
			resize : true,
			sortable : true
		}, opts || {});

		var table = this;
		var oldPos = 0.0;

		function resetTableSizes(change, columnIndex) {
			var tableId = table.attr('id');
			var myWidth = $('#' + tableId + ' TR TH').eq(columnIndex).outerWidth();
			var nxWidth = $('#' + tableId + ' TR TH').eq(columnIndex + 1).outerWidth();

			$('#' + tableId + ' thead th').eq(columnIndex + 1).css('width', nxWidth - change);
			$('#' + tableId + ' thead th').eq(columnIndex).css('width', myWidth + change);
			
			resetHandler();
		}

		function resetHandler() {
			var tableId = table.attr('id');
			table.find(' TR:first TH').each(function(index) {
				var td = $(this);
				var newSliderPosition = td.offset().left + td.outerWidth();
				$("#" + tableId + "_id" + (index + 1)).css({
					left : newSliderPosition,
					height : table.height() + 'px'
				});
			});
		}
		if(opts.refresh){
			resetHandler();
		}else if (opts.resize) {
			var numberOfColumns = table.find('TR:first TH').size();
			var tableId = table.attr('id');

			for ( var i = 1; i < numberOfColumns; i++) {
				$('<div class="draghandle" id="' + tableId + '_id' + i + '"></div>')
						.insertBefore(table)
						.data('tableid', tableId)
						.data('myindex', i)
						.draggable(
								{
									axis : "x",
									start : function(event, ui) {
										var tableId = ($(this).data('tableid'));
										$(this).toggleClass("dragged");
										oldPos = ui.position.left;
										$(this).css(
												{
													height : $('#' + tableId)
															.height()
															+ 'px'
												});
									},
									stop : function(event, ui) {
										$(this).toggleClass("dragged");
										var newPos = ui.position.left;
										var index = $(this).data("myindex");
										resetTableSizes(newPos - oldPos,
												index - 1);
									}
								});
			}
			resetHandler();
		}

	};
})(jQuery);
