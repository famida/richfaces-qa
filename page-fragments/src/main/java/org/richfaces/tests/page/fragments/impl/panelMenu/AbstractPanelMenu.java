package org.richfaces.tests.page.fragments.impl.panelMenu;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

import com.google.common.base.Predicate;

public abstract class AbstractPanelMenu implements PanelMenu, PanelMenuGroup {

    public static final String CSS_EXPANDED_SUFFIX = "-exp";
    public static final String CSS_TRANSPARENT_SUFFIX = "-transparent";
    public static final String CSS_SELECTED_SUFFIX = "-sel";
    public static final String CSS_HOVERED_SUFFIX = "-hov";
    public static final String CSS_DISABLED_SUFFIX = "-dis";
    public static final String CSS_COLLAPSED_SUFFIX = "-colps";
    private static final String HEADER_SELECTOR_TO_INVOKE_EVENT_ON = "> div[class*=hdr]";

    @ArquillianResource
    private JavascriptExecutor executor;
    @Drone
    private WebDriver browser;
    private AdvancedInteractions advancedInteractions;

    private Event expandEvent = Event.CLICK;
    private Event collapseEvent = Event.CLICK;

    @Override
    public PanelMenuItem selectItem(ChoicePicker picker) {
        WebElement itemRoot = picker.pick(getMenuItems());
        ensureElementExist(itemRoot);
        ensureElementIsEnabledAndVisible(itemRoot);
        itemRoot.click();
        return Graphene.createPageFragment(RichFacesPanelMenuItem.class, itemRoot);
    }

    @Override
    public PanelMenuItem selectItem(String header) {
        return selectItem(ChoicePickerHelper.byVisibleText().match(header));
    }

    @Override
    public PanelMenuItem selectItem(int index) {
        return selectItem(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public PanelMenuGroup expandGroup(ChoicePicker picker) {
        WebElement groupRoot = picker.pick(getMenuGroups());
        ensureElementExist(groupRoot);
        ensureElementIsEnabledAndVisible(groupRoot);
        WebElement groupHeader = getHeaderElementDynamically(groupRoot);
        executeEventOn(expandEvent, groupHeader);
        waitUntilExpanded(groupHeader);
        return Graphene.createPageFragment(RichFacesPanelMenuGroup.class, groupRoot);
    }

    @Override
    public PanelMenuGroup expandGroup(String header) {
        return expandGroup(ChoicePickerHelper.byVisibleText().match(header));
    }

    @Override
    public PanelMenuGroup expandGroup(int index) {
        return expandGroup(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public void collapseGroup(ChoicePicker picker) {
        WebElement groupRoot = picker.pick(getMenuGroups());
        ensureElementExist(groupRoot);
        ensureElementIsEnabledAndVisible(groupRoot);
        WebElement groupHeader = getHeaderElementDynamically(groupRoot);
        executeEventOn(collapseEvent, groupHeader);
        waitUntilCollapsed(groupHeader);
    }

    @Override
    public void collapseGroup(String header) {
        collapseGroup(ChoicePickerHelper.byVisibleText().match(header));
    }

    @Override
    public void collapseGroup(int index) {
        collapseGroup(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public PanelMenu expandAll() {
        // TODO
        return null;
    }

    @Override
    public PanelMenu collapseAll() {
        // TODO
        return null;
    }

    public abstract List<WebElement> getMenuItems();

    public abstract List<WebElement> getMenuGroups();

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions {

        public void setExpandEvent(Event event) {
            expandEvent = event;
        }

        public void setCollapseEvent(Event event) {
            collapseEvent = event;
        }

        public boolean isGroupExpanded(WebElement groupRoot) {
            return isGroupExpanded(getHeaderElementDynamically(groupRoot));
        }

        public WebElement getHeaderElement(WebElement groupRoot) {
            return getHeaderElementDynamically(groupRoot);
        }
    }

    private boolean isGroupExpanded(WebElement group) {
        return group.getAttribute("class").contains(CSS_EXPANDED_SUFFIX);
    }

    private void ensureElementIsEnabledAndVisible(WebElement element) {
        checkElementIsVisible(element);
        if (isDisabled(element)) {
            throw new IllegalArgumentException("Element " + element + " can not be interacted with, as it is disabled.");
        }
    }

    private boolean isDisabled(WebElement group) {
        return group.getAttribute("class").contains(CSS_DISABLED_SUFFIX);
    }

    private void checkElementIsVisible(WebElement element) {
        if (!new WebElementConditionFactory(element).isVisible().apply(browser)) {
            throw new IllegalArgumentException("Element: " + element + " must be visible before interacting with it!");
        }
    }

    private void waitUntilExpanded(final WebElement group) {
        Graphene.waitModel().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return isGroupExpanded(group);
            }
        });
    }

    private void waitUntilCollapsed(final WebElement group) {
        Graphene.waitModel().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return !isGroupExpanded(group);
            }
        });
    }

    private void executeEventOn(Event event, WebElement element) {
        Utils.triggerJQ(executor, event.getEventName(), element);
    }

    private WebElement getHeaderElementDynamically(WebElement element) {
        return element.findElement(ByJQuery.jquerySelector(HEADER_SELECTOR_TO_INVOKE_EVENT_ON));
    }

    private void ensureElementExist(WebElement element) {
        if (element == null) {
            throw new IllegalArgumentException("Group/item must exist!");
        }
    }
}
