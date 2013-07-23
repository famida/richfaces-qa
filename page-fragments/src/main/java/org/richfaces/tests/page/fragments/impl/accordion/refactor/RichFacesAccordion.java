package org.richfaces.tests.page.fragments.impl.accordion.refactor;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RichFacesAccordion implements SwitchableComponent {

    @FindBy(className = "rf-ac-itm")
    private List<WebElement> items;

    @FindBy
    private List<RichFacesAccordionItem> accordionItems;

    private SwitchType switchType = SwitchType.CLIENT;
    private AdvancedInteractions advancedInteractions;

    public enum SwitchType {
        CLIENT,
        AJAX,
        HTTP
    }

    @Override
    public RichFacesAccordionItem switchTo(Picker picker) {
        WebElement switcher = picker.pick(items);
        if (switcher == null) {
            throw new IllegalArgumentException("No such accordion item which fulfill the conditions from picker: " + picker);
        }
        switchTo(switcher);
        return Graphene.createPageFragment(RichFacesAccordionItem.class, switcher);
    }

    @Override
    public RichFacesAccordionItem switchTo(String header) {
        WebElement switcher = null;
        for (WebElement element : items) {
            if (element.getText().equals(header)) {
                switcher = element;
                break;
            }
        }
        if (switcher == null) {
            throw new IllegalArgumentException("No such accordion item with header: " + header);
        }
        switchTo(switcher);
        return Graphene.createPageFragment(RichFacesAccordionItem.class, switcher);
    }

    @Override
    public RichFacesAccordionItem switchTo(int index) {
        WebElement switcher = null;
        try {
            switcher = items.get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("Trying to switch to accordion item with index: " + index
                + ", however, there are only " + items.size() + " items.");
        }
        return Graphene.createPageFragment(RichFacesAccordionItem.class, switcher);
    }

    private void switchTo(WebElement switcher) {
        switch (switchType) {
            case CLIENT:
                switcher.click();
                Graphene.waitGui();
                break;
            case AJAX:
                guardAjax(switcher).click();
                break;
            case HTTP:
                guardHttp(switcher).click();
                break;
        }
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions {

        public void setSwitchType(SwitchType newSwitchType) {
            switchType = newSwitchType;
        }
    }
}