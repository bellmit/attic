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
package ca.grimoire.jnoise.config;

import java.util.LinkedList;

import ca.grimoire.jnoise.config.beans.BeanException;
import ca.grimoire.jnoise.config.beans.BeanRegistry;
import ca.grimoire.jnoise.config.beans.NamedAttributeProxy;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX event handler for parsing noise documents. Builds a tree of Elements and
 * ModuleBuilders based on the document events.
 */
final class NoiseHandler extends DefaultHandler {

  private static void applyAttributes (Element element, Attributes attributes)
      throws BeanException, SAXException {
    assert (element != null);
    assert (attributes != null);

    NamedAttributeProxy proxy = new NamedAttributeProxy (element);
    for (int attribute = 0; attribute < attributes.getLength (); ++attribute) {
      String name = toName (attributes.getLocalName (attribute), attributes
          .getQName (attribute));

      try {
        proxy.setProperty (name, attributes.getValue (attribute));
      } catch (Exception e) {
        throw new SAXException (e);
      }
    }
  }

  private static String toName (String localName, String qName) {
    return localName != null ? localName : qName;
  }

  /**
   * Creates a new noise content handler.
   * 
   * @param tags
   *          the registry providing elements for tag names.
   */
  public NoiseHandler (BeanRegistry<Element> tags) {
    assert (tags != null);
    this.tags = tags;
  }

  /**
   * Handles end of document events. Makes sure the root element was seen.
   * 
   * @throws SAXException
   *           if the document is incomplete.
   */
  @Override
  public void endDocument () throws SAXException {
    if (root == null)
      throw new SAXException ("No root element found");

    assert (stack.isEmpty ());
    documentComplete = true;
  }

  /**
   * Handles element end events.
   * 
   * @param uri
   *          the namespace URI for the element.
   * @param localName
   *          the local name of the element.
   * @param qName
   *          the qualified name of the element.
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  @Override
  public void endElement (String uri, String localName, String qName) {
    root = popElement ();
  }

  /**
   * Returns the root element of the last document parsed. This is only valid
   * after endDocument is called; if startDocument is called it becomes invalid
   * again until the next call to endDocument.
   * 
   * @return the root element of the last document parsed.
   */
  public Element getRootElement () {
    if (!documentComplete)
      throw new IllegalStateException ("Incomplete noise document");

    return root;
  }

  /**
   * Handles start of document events. Clears out some state from the end of the
   * previous document.
   */
  @Override
  public void startDocument () {
    documentComplete = false;
    root = null;
    stack.clear ();
  }

  /**
   * Handles element start events. Creates an Element or ModuleBuilder for the
   * tag and applies attributes.
   * 
   * @param uri
   *          the namespace URI for the element. Unused.
   * @param localName
   *          the local name for the element.
   * @param qName
   *          the qualified name for the element.
   * @param attributes
   *          the element's attributes.
   * @throws SAXException
   *           if the tag name is invalid or an attribute not appropriate for
   *           the tag is provided.
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  @Override
  public void startElement (String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
    String name = toName (localName, qName);

    try {
      Element element = tags.create (name);
      applyAttributes (element, attributes);
      pushElement (element);

    } catch (BeanException be) {
      throw new SAXException (be);
    } catch (BuilderException be) {
      throw new SAXException (be);
    }
  }

  private Element popElement () {
    assert (!stack.isEmpty ());

    return stack.removeLast ();
  }

  private void pushElement (Element element) throws BuilderException {
    if (!stack.isEmpty ())
      stack.getLast ().addChild (element);

    stack.add (element);
  }

  private boolean                     documentComplete = false;
  private Element                     root             = null;
  private final LinkedList<Element>   stack            = new LinkedList<Element> ();
  private final BeanRegistry<Element> tags;
}
