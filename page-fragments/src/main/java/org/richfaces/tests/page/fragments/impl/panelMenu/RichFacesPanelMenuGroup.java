package org.richfaces.tests.page.fragments.impl.panelMenu;

import java.util.List;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;

public class RichFacesPanelMenuGroup extends AbstractPanelMenu {

    @FindBy(jquery = ".rf-pm-gr:visible")
    private List<WebElement> menuGroups;
    @FindBy(jquery = ".rf-pm-itm:visible")
    private List<WebElement> menuItems;
    @FindBy(css = "td[class*=rf-pm-][class*=-gr-lbl]")
    private WebElement label;

    @FindBy(jquery = "td[class*=rf-pm-][class*=-gr-ico] :visible[class*=rf-pm-ico-]")
    private WebElement leftIcon;
    @FindBy(jquery = "td[class*=rf-pm-][class*=-gr-exp-ico] :visible[class*=rf-pm-ico-]")
    private WebElement rightIcon;

    @Root
    private WebElement root;
    private AdvancedInteractions advancedInteractions;

    @Override
    public List<WebElement> getMenuItems() {
        return menuItems;
    }

    @Override
    public List<WebElement> getMenuGroups() {
        return menuGroups;
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions extends AbstractPanelMenu.AdvancedInteractions {

        public boolean isExpanded() {
            return super.isGroupExpanded(root);
        }

        public WebElement getLabel() {
            return label;
        }

        public WebElement getRootElement() {
            return root;
        }

        public boolean isTransparent(WebElement icon) {
            return icon.getAttribute("class").contains("-transparent");
        }

        public WebElement getLeftIcon() {
            return leftIcon;
        }

        public WebElement getRightIcon() {
            return rightIcon;
        }

        public WebElement getHeaderElement() {
            return super.getHeaderElement(root);
        }
    }
}