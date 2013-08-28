package org.richfaces.tests.metamer.ftest.richInplaceSelect;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.inplaceSelectAttributes;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.RichFacesInplaceSelect;
import org.testng.annotations.Test;

public class TestRefactoredInplaceSelectShowcase extends AbstractWebDriverTest {

    @FindBy(css = ".rf-is")
    private RichFacesInplaceSelect inplaceSelect;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceSelect/simple.xhtml");
    }

    @Test
    public void testInplaceSelectCancelConfirm() {
        inplaceSelect.select(2);

        String selectedValue = inplaceSelect.getTextInput().getStringValue();
        assertEquals(selectedValue, "Arizona");

        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnBlur, false);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, false);

        inplaceSelect.select(2).confirm();
        inplaceSelect.select(3).cancel();
        selectedValue = inplaceSelect.getTextInput().getStringValue();
        assertEquals(selectedValue, "Arizona");

        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, true);
        inplaceSelect.select(3).confirmByControlls();
        selectedValue = inplaceSelect.getTextInput().getStringValue();
        assertEquals(selectedValue, "Arkansas");
    }
}
