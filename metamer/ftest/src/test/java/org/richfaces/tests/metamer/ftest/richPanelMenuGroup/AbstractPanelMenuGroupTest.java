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
package org.richfaces.tests.metamer.ftest.richPanelMenuGroup;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.mode;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.panelMenuGroupAttributes;
import static org.richfaces.ui.common.Mode.ajax;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
public abstract class AbstractPanelMenuGroupTest extends AbstractPanelMenuCommonTest {

    @Page
    PanelMenuGroupPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenuGroup/simple.xhtml");
    }

    @Override
    public MetamerPage getPage() {
        return page;
    }

    @BeforeMethod
    public void setupModes() {
        panelMenuGroupAttributes.set(mode, ajax);
        page.topGroup.setMode(ajax);
    }
}
