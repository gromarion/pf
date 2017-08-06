package com.itba;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Separate startup class for people that want to run the examples directly. Use parameter
 * -Dcom.sun.management.jmxremote to startup JMX (and e.g. connect with jconsole).
 */
public class Start
{
	/**
	 * Main function, starts the jetty server.
	 *
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.setProperty("wicket.configuration", "development");

		Server server = new Server();

		ServerConnector http = new ServerConnector(server);
		http.setPort(8080);
		http.setIdleTimeout(1000 * 60 * 60);

		server.addConnector(http);

		WebAppContext bb = new WebAppContext();
		bb.setServer(server);
		bb.setContextPath("/");
		bb.setWar("src/main/webapp");

		server.setHandler(bb);

		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
		server.addEventListener(mBeanContainer);
		server.addBean(mBeanContainer);

		try
		{
			server.start();
			server.join();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(100);
		}
	}
}
