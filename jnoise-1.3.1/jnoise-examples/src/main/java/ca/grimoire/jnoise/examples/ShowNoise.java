/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 * 
 * Copyright (C) 2005 Owen Jacobson <angrybaldguy@gmail.com>
 */
package ca.grimoire.jnoise.examples;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

import javax.swing.*;

import ca.grimoire.jnoise.config.ConfigurationException;
import ca.grimoire.jnoise.config.NoiseConfiguration;
import ca.grimoire.jnoise.config.XMLConfigurationLoader;
import ca.grimoire.jnoise.models.PlaneModel;
import ca.grimoire.jnoise.modules.Module;

/**
 * Example program that displays selectable noise types in a window.
 */
public final class ShowNoise {
  // Image size
  private static final int    HEIGHT                  = 500;
  private static final int    WIDTH                   = 500;

  // Noise-to-plane mapping.
  private static final double OX                      = -2.5, OY = -2.5, OZ = 0;
  private static final double UX                      = 5, UY = 0, UZ = 0;
  private static final double VX                      = 0, VY = 5, VZ = 0;

  // Configuration
  private static final String BUILTIN_CONFIG_RESOURCE = "ca/grimoire/jnoise/examples/modules.xml";
  private static final String USER_CONFIG_FILE        = "jnoise.xml";
  private static final String USER_CONFIG_RESOURCE    = "jnoise.xml";

  /**
   * Run the example application. This pops up a 500x500 image of some perlin
   * noise, with 100 pixels per subjective unit square.
   * 
   * @param args
   *          unused command line args.
   * @throws ConfigurationException
   *           if the example configuration is unloadable.
   * @throws IOException
   *           if the example configuration is unavailable.
   */
  public static void main (String[] args) throws IOException,
      ConfigurationException {
    final Module[] modules = loadModules ();

    Image image = createImage (modules[0]);

    // Window setup to display the iamge.
    final JFrame window = new JFrame ("Showing Noise");
    final ImageIcon icon = new ImageIcon ();
    final JComboBox selector = new JComboBox (modules);

    window.getContentPane ().setLayout (new BorderLayout ());
    window.getContentPane ().add (new JLabel (icon), BorderLayout.CENTER);
    window.getContentPane ().add (selector, BorderLayout.NORTH);

    selector.addActionListener (new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        icon.setImage (createImage (modules[selector.getSelectedIndex ()]));
        window.repaint ();
      }
    });

    icon.setImage (image);

    window.setResizable (false);
    window.pack ();
    window.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    window.setVisible (true);
  }

  private static Module[] loadModules () throws IOException,
      ConfigurationException {
    NoiseConfiguration config = loadConfiguration ();
    List<Module> moduleList = config.getNoiseModules ();

    return moduleList.toArray (new Module[moduleList.size ()]);
  }

  private static NoiseConfiguration loadConfiguration () throws IOException,
      ConfigurationException {
    XMLConfigurationLoader configurator = new XMLConfigurationLoader ();
    InputStream in = openConfigStream ();
    try {
      InputStreamReader reader = new InputStreamReader (in);
      return configurator.loadConfiguration (reader);
    } finally {
      in.close ();
    }
  }

  private static InputStream openConfigStream () {
    try {
      return openFileStream (USER_CONFIG_FILE);

    } catch (IOException ioe) {
      ClassLoader classloader = Thread.currentThread ()
          .getContextClassLoader ();
      InputStream resStream = openResourceStream (classloader,
          USER_CONFIG_RESOURCE);
      if (resStream == null)
        resStream = openResourceStream (classloader, BUILTIN_CONFIG_RESOURCE);
      return resStream;
    }
  }

  private static InputStream openFileStream (String file)
      throws FileNotFoundException {
    return new FileInputStream (file);
  }

  private static InputStream openResourceStream (ClassLoader classloader,
      String resource) {
    return classloader.getResourceAsStream (resource);
  }

  static Image createImage (Module module) {
    // Generate an image.
    PlaneModel plane = new PlaneModel (module, OX, OY, OZ, UX, UY, UZ, VX, VY,
        VZ);
    ImageBuilder builder = new ImageBuilder (plane, 1, 1);
    Image image = builder.createImage (WIDTH, HEIGHT);
    return image;
  }
}
