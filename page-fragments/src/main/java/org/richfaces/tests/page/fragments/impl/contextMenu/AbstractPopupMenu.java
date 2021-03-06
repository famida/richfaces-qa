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
package org.richfaces.tests.page.fragments.impl.contextMenu;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.utils.Actions;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapper;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapperImpl;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

import com.google.common.base.Optional;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class AbstractPopupMenu implements PopupMenu {

    @Drone
    private WebDriver browser;

    @Root
    private WebElement root;

    private final AdvancedPopupMenuInteractions advancedInteractions = new AdvancedPopupMenuInteractions();

    private long _hideWaitUntilIsNotVisibleTimeout = -1;
    private long _showWaitUntilIsVisibleTimeout = -1;

    /* ************************************************************************************************
     * Abstract methods
     */
    /**
     * Returns the popup element of the menu
     *
     * @return
     */
    protected abstract WebElement getMenuPopupInternal();

    /**
     * Returns all elements of this menu
     *
     * @return
     */
    protected abstract List<WebElement> getMenuItemElementsInternal();

    /**
     * Returns the name of the actual page fragment.
     *
     * @return
     */
    protected String getNameOfFragment() {
        return getClass().getSimpleName();
    }

    protected WebElement getRootElement() {
        return root;
    }

    protected abstract WebElement getScriptElement();

    /* ************************************************************************************************
     * API
     */
    public AdvancedPopupMenuInteractions advanced() {
        return advancedInteractions;
    }

    @Override
    public void selectItem(ChoicePicker picker) {
        advanced().show();
        picker.pick(getMenuItemElementsInternal()).click();
    }

    @Override
    public void selectItem(String header) {
        selectItem(ChoicePickerHelper.byVisibleText().match(header));
    }

    @Override
    public void selectItem(int index) {
        selectItem(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public void selectItem(ChoicePicker picker, WebElement target) {
        advanced().setupTarget(target);
        selectItem(picker);
    }

    @Override
    public void selectItem(String header, WebElement target) {
        advanced().setupTarget(target);
        selectItem(header);
    }

    @Override
    public void selectItem(int index, WebElement target) {
        advanced().setupTarget(target);
        selectItem(index);
    }

    /* ****************************************************************************************************
     * Nested classes
     */
    public class AdvancedPopupMenuInteractions {

        private final Event DEFAULT_INVOKE_EVENT = Event.CONTEXTCLICK;
        private Event invokeEvent = DEFAULT_INVOKE_EVENT;

        private static final int DEFAULT_SHOWDELAY = 100;
        private int showDelay = DEFAULT_SHOWDELAY;

        private WebElement target;

        /**
         * Dismisses currently displayed popup menu. If no popup menu is currently displayed an exception is thrown.
         *
         * @throws IllegalStateException when no popup menu is displayed in the time of invoking
         */
        public void hide() {
            if (!getMenuPopupInternal().isDisplayed()) {
                throw new IllegalStateException("You are attemting to dismiss the " + getNameOfFragment() + ", however, no "
                    + getNameOfFragment() + " is displayed at the moment!");
            }
            browser.findElement(Utils.BY_HTML).click();
            waitUntilIsNotVisible().withTimeout(getHideWaitUntilIsNotVisibleTimeout(), TimeUnit.SECONDS).perform();
        }

        /**
         * Returns menu items elements. One needs to invoke popup menu in order to work with them. Note that some of the
         * elements may not become visible by just invoking the popup menu (e.g. popup menu items with sub items)
         *
         * @return the popup menu items
         */
        public List<WebElement> getItemsElements() {
            return Collections.unmodifiableList(getMenuItemElements());
        }

        /**
         * Returns all elements of this menu
         *
         * @return
         */
        public List<WebElement> getMenuItemElements() {
            return Collections.unmodifiableList(getMenuItemElementsInternal());
        }

        public WebElement getMenuPopup() {
            return getMenuPopupInternal();
        }

        protected int getShowDelay() {
            return showDelay;
        }

        public WebElement getTargetElement() {
            if (target == null) {
                setupTarget();
            }
            return target;
        }

        /**
         * Invokes popup menu in the middle of the currently set target. By default it is presumed that popup menu is invoked by
         * right click. To change this behavior use <code>setInvoker()</code> method. You have to have a target set before
         * invocation of this method.
         *
         * @see #setupInvoker(PopupMenuInvoker)
         * @see #setTarget(WebElement)
         */
        public void show() {
            show(getTargetElement());
        }

        /**
         * Invokes popup menu in the middle of the given target. By default it is presumed that popup menu is invoked by right
         * click. To change this behavior use <code>setInvoker()</code> method. It also works with the default value of
         * <code>showDelay == 50ms</code>. Use <code>#setShowDelay</code> if this value is different for this menu.
         *
         * @param givenTarget
         * @see #setupInvoker(PopupMenuInvoker)
         * @see #setupShowDelay(int)
         */
        public void show(WebElement givenTarget) {
            new Actions(browser)
                .moveToElement(givenTarget)
                .triggerEventByWD(invokeEvent, givenTarget).perform();

            advanced().waitUntilIsVisible().withTimeout(getShowWaitUntilIsVisibleTimeout(), TimeUnit.SECONDS).perform();
        }

        /**
         * Invokes popup menu on a given point within the given target. By default it is presumed that popup menu is invoked by
         * right click. To change this behavior use <code>setInvoker()</code> method.
         *
         * @param givenTarget
         * @param location
         * @see #setupInvoker(PopupMenuInvoker)
         */
        public void show(WebElement givenTarget, Point location) {
            throw new UnsupportedOperationException("File a feature request to have this, or even better implement it:)");
//            actions
//                .moveToElement(givenTarget)
//                .moveByOffset(location.getX(), location.getY())
//                .triggerEventByWD(invokeEvent, givenTarget).perform();
//
//            advanced().waitUntilIsVisible().perform();
        }

        public void setupShowEvent() {
            invokeEvent = DEFAULT_INVOKE_EVENT;
        }

        public void setupShowEvent(Event newShowEvent) {
            if (newShowEvent == null) {
                throw new IllegalArgumentException("Parameter newInvokeEvent can not be null!");
            }
            invokeEvent = newShowEvent;
        }

        public void setupShowEventFromWidget() {
            Optional<String> event = Utils.getJSONValue2(getScriptElement(), "showEvent");
            invokeEvent = new Event(event.or(DEFAULT_INVOKE_EVENT.getEventName()));
        }

        public void setupShowDelay() {
            showDelay = DEFAULT_SHOWDELAY;
        }

        /**
         * Sets the delay which is between showevent observing and the menu opening
         *
         * @param newShowDelay
         */
        public void setupShowDelay(int newShowDelay) {
            if (showDelay < 0) {
                throw new IllegalArgumentException("Can not be negative!");
            }
            showDelay = newShowDelay;
        }

        public void setupTarget() {
            target = root;
        }

        public void setupTarget(WebElement target) {
            this.target = target;
        }

        public void setupTargetFromWidget() {
            String targetId = Utils.getJSONValue(getScriptElement(), "target");
            if (targetId != null) {
                target = browser.findElement(By.id(targetId));
            } else {
                target = root;
            }
        }

        public void setupHideWaitUntilIsNotVisibleTimeout(long timeout) {
            _hideWaitUntilIsNotVisibleTimeout = timeout;
        }

        public long getHideWaitUntilIsNotVisibleTimeout() {
            return _hideWaitUntilIsNotVisibleTimeout == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _hideWaitUntilIsNotVisibleTimeout;
        }

        public void setupShowWaitUntilIsVisibleTimeout(long timeout) {
            _showWaitUntilIsVisibleTimeout = timeout;
        }

        public long getShowWaitUntilIsVisibleTimeout() {
            return _showWaitUntilIsVisibleTimeout == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _showWaitUntilIsVisibleTimeout;
        }

        /**
         * Waits until the popup menu is visible. It takes into account the <code>showDelay</code> which has default value 50ms.
         *
         * @see #setupShowDelay(int)
         */
        public WaitingWrapper waitUntilIsNotVisible() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until().element(getMenuPopupInternal()).is().not().visible();
                }
            }.withMessage("Waiting for menu to hide.");
        }

        public WaitingWrapper waitUntilIsVisible() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until().element(getMenuPopup()).is().visible();
                }
            }.withMessage("The " + getNameOfFragment() + " did not show in the given timeout!")
                .withTimeout(showDelay + 4000, TimeUnit.MILLISECONDS);
        }
    }
}
