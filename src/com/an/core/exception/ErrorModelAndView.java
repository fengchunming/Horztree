/**
 *
 */
package com.an.core.exception;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Karas
 * @date 2012-6-2
 */
public class ErrorModelAndView extends ModelAndView {

    public ErrorModelAndView(Exception e) {
        this.addObject("name", e.getClass().getSimpleName());
        this.addObject("exception", e.getMessage());
    }
}
