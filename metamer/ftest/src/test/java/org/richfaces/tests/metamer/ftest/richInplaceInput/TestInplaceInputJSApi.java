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
package org.richfaces.tests.metamer.ftest.richInplaceInput;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.inplaceInput.RichFacesInplaceInput;
import org.richfaces.tests.page.fragments.impl.inplaceInput.RichFacesInplaceInput.State;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.testng.annotations.Test;

/**
 * Test case for JavaScript API on page faces/components/richInplaceInput/simple.xhtml.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInplaceInputJSApi extends AbstractWebDriverTest {

    @FindBy(css = "span[id$=inplaceInput]")
    private RichFacesInplaceInput inplaceInput;
    //
    @FindBy(css = "input[id$=value]")
    private WebElement output;
    //
    @FindBy(id = "cancel")
    private WebElement cancelButton;
    @FindBy(id = "getInput")
    private WebElement getInputButton;
    @FindBy(id = "getValue")
    private WebElement getValueButton;
    @FindBy(id = "isEditState")
    private WebElement isEditStateButton;
    @FindBy(id = "isValueChanged")
    private WebElement isValueChangedButton;
    @FindBy(id = "save")
    private WebElement saveButton;
    @FindBy(id = "setValue")
    private WebElement setValueButton;
    //
    private static final String SOME_TEXT = "another completely different string";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceInput/simple.xhtml");
    }

    @Test
    public void cancel() {
        String defaultText = inplaceInput.advanced().getLabelValue();
        inplaceInput.type(SOME_TEXT);
        fireEvent(cancelButton, Event.MOUSEOVER);
        assertEquals(inplaceInput.advanced().getLabelValue(), defaultText);
        assertFalse(inplaceInput.advanced().isInState(State.CHANGED));
    }

    @Test
    public void getInput() {
        //the button has onclick action, it selects the input and clicks on it
        //so the state of inplace input is set to active
        getInputButton.click();
        waiting(100);
        inplaceInput.advanced().isInState(State.ACTIVE);
    }

    @Test
    public void getValue() {
        getValueButton.click();
        assertEquals(getValueFromOutput(), inplaceInput.advanced().getLabelValue(), "Default value.");
    }

    private String getValueFromOutput() {
        return this.output.getAttribute("value");
    }

    @Test
    public void isEditState() {
        fireEvent(isEditStateButton, Event.MOUSEOVER);
        assertEquals(getValueFromOutput(), "false");
        inplaceInput.type(" ");
        fireEvent(isEditStateButton, Event.MOUSEOVER);
        assertEquals(getValueFromOutput(), "true");
    }

    @Test
    public void isValueChangedButton() {
        isValueChangedButton.click();
        assertEquals(getValueFromOutput(), "false");
        MetamerPage.waitRequest(inplaceInput.type(SOME_TEXT),
                WaitRequestType.XHR).confirm();
        isValueChangedButton.click();
        assertEquals(getValueFromOutput(), "true");
    }

    @Test
    public void save() {
        inplaceInput.type(SOME_TEXT);
        fireEvent(saveButton, Event.MOUSEOVER);
        assertEquals(inplaceInput.advanced().getLabelValue(), SOME_TEXT);
        assertTrue(inplaceInput.advanced().isInState(State.CHANGED));
    }

    @Test
    public void setValue() {
        String expected = "Completely different value";
        setValueButton.click();
        assertEquals(inplaceInput.advanced().getLabelValue(), expected);
    }
}
