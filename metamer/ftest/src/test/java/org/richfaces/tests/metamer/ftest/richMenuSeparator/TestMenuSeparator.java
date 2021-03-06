/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.richMenuSeparator;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.menuSeparatorAttributes;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richMenuSeparator/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestMenuSeparator extends AbstractWebDriverTest {

    @FindBy(css = "div.rf-ddm-sep")
    private List<WebElement> separators;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richMenuSeparator/simple.xhtml");
    }

    @Test
    public void testRendered() {
        assertEquals(separators.size(), 2, "There should be two separators on the page -- after 'Open Recent' and after 'Close'.");

        menuSeparatorAttributes.set(MenuSeparatorAttributes.rendered, Boolean.FALSE);
        assertEquals(separators.size(), 1, "There should be only one separator on the page -- after 'Close'.");

        menuSeparatorAttributes.set(MenuSeparatorAttributes.rendered, Boolean.TRUE);
        assertEquals(separators.size(), 2, "There should be two separators on the page -- after 'Open Recent' and after 'Close'.");
    }
}
