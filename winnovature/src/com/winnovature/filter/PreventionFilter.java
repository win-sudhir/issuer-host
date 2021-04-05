package com.winnovature.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
@WebFilter("/PreventionFilter")
public class PreventionFilter implements Filter 
{
	private String mode = "DENY";

	// Add X-FRAME-OPTIONS response header to tell any other browsers who not to display this //content in a frame.
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse)response;
		res.addHeader("X-FRAME-OPTIONS", mode );
		res.addHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		res.addHeader("Pragma", "no-cache");
		// res.addHeader("Expires", "0");
		chain.doFilter(request, response);

	}

	public void destroy() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String configMode = filterConfig.getInitParameter("mode");
		if(configMode!=null)
			mode = configMode;
		
	}

	
	

}