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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jStatus;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.a4jStatus.ViewUsagePage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jStatusSimple extends AbstractWebDriverTest {

    @Page
    private ViewUsagePage page;

    @Test
    public void testSubmitUser() {
        getPage().getUsernameInput().click();
        getPage().getUsernameInput().sendKeys("something");
        getPage().getAddressInput().click();
        getPage().getAddressInput().sendKeys("something");
        getPage().getUserSubmit().click();
        Graphene.waitAjax()
            .withMessage("After submitting the username and the address, the request image should be present.")
            .until(new WebElementConditionFactory(getPage().getRequestImage()).isPresent());
        Graphene.waitAjax()
            .withMessage("After submitting the username and the address, the output text should be present.")
            .until(new WebElementConditionFactory(getPage().getUserOutput()).isPresent());
        assertEquals(getPage().getUserOutput().getText(), "User stored successfully");
    }

    @Test
    public void testTypeAddress() {
        getPage().getAddressInput().click();
        getPage().getAddressInput().sendKeys("something");
        Graphene.waitAjax()
            .withMessage("After typing the address, the request image should be present.")
            .until(new WebElementConditionFactory(getPage().getRequestImage()).isPresent());
    }


    @Test
    public void testTypeUsername() {
        getPage().getUsernameInput().click();
        getPage().getUsernameInput().sendKeys("something");
        Graphene.waitAjax()
            .withMessage("After typing the username, the request image should be present.")
            .until(new WebElementConditionFactory(getPage().getRequestImage()).isPresent());
    }

    @Override
    protected ViewUsagePage getPage() {
        return page;
    }

}
