// Sticky v1.0 by Daniel Raftery
// http://thrivingkings.com/sticky
//
// http://twitter.com/ThrivingKings

(function ($) {

    // Using it without an object
    $.sticky = function (note, options, callback) {
        return $.fn.sticky(note, options, callback);
    };

    $.startLoading = function (){
        NProgress.start();
    };

    $.stopLoading = function (){
        NProgress.done();
    };

    $.fn.sticky = function (note, options, callback) {
        // Default settings
        var position = 'top-right'; // top-left, top-right, bottom-left, or
        // bottom-right

        var settings = {
            'speed': 'fast', // animations: fast, slow, or integer
            'duplicates': false, // true or false
            'autoclose': 3000,
            'warning': false,
            'progress': false
            // integer or false
        };

        // Passing in the object instead of specifying a note
        if (!note) {
            note = this.html();
        }

        if (options) {
            $.extend(settings, options);
        }

        // Variables
        var display = true;
        var duplicate = 'no';

        // Somewhat of a unique ID
        var uniqID = Math.floor(Math.random() * 99999);

        // Handling duplicate notes and IDs
        $('.sticky-note').each(function () {
            if ($(this).html() == note && $(this).is(':visible')) {
                duplicate = 'yes';
                if (!settings['duplicates']) {
                    display = false;
                }
                if(settings['progress']){
                    $(this).siblings('.progressbar').progressbar('value', settings.value);
                }
            }
            if ($(this).attr('id') == uniqID) {
                uniqID = Math.floor(Math.random() * 9999999);
            }
        });

        // Make sure the sticky queue exists
        if ($('.sticky-queue').length == 0) {
            $('body').append(
                    '<div class="sticky-queue ' + position + '"></div><audio id="splayer" src="images/sticky.mp3"></audio>');
        }

        // Can it be displayed?
        if (display) {
            // Building and inserting sticky note
            $uuiqID = $('<div class="sticky border-' + position + '" id="' + uniqID + '"></div>');
            $('.sticky-queue').prepend($uuiqID);
            $uuiqID.append(
                    '<img src="images/close.png" class="sticky-close" rel="' + uniqID + '" title="Close" />');
            $uuiqID.append(
                    '<div class="sticky-note" rel="' + uniqID + '">' + note + '</div>');
            if(settings.progress) {
                $('<div class="progressbar" style="margin-top:2px;"><div class="progress-label"></div></div>').progressbar({
                    value: false,
                    change: function() {
                        $(this).find('.progress-label').text( $(this).progressbar( "value" ) + "%" );
                    },
                    complete: function(){
                        $(this).find('.progress-label').text( "上传完成" );
                    }
                }).appendTo($uuiqID);
            }

            // Smoother animation
            var height = $uuiqID.height();
            $uuiqID.css('height', height);

            $uuiqID.slideDown(settings['speed']);
            if (settings['warning']) {
                $('#splayer').get(0).play();
            }
            display = true;
        }

        // Listeners
        $('.sticky').ready(function () {
            // If 'autoclose' is enabled, set a timer to close the
            // sticky
            if (settings['autoclose']) {
                $uuiqID.delay(settings['autoclose']).fadeOut(
                    settings['speed']);
            }
        });
        // Closing a sticky
        $('.sticky-close').click(function () {
            $('#' + $(this).attr('rel')).dequeue().fadeOut(settings['speed']);
        });

        // Callback data
        var response = {
            'id': uniqID,
            'duplicate': duplicate,
            'displayed': display,
            'position': position
        };

        // Callback function?
        if (callback) {
            callback(response);
        } else {
            return (response);
        }

    }
})(jQuery);