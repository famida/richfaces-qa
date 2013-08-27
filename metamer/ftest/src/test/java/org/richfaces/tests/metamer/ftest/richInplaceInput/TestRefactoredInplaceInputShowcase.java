package org.richfaces.tests.metamer.ftest.richInplaceInput;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.inplaceInputAttributes;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.page.fragments.impl.inplaceInput.RichFacesInplaceInput;
import org.testng.annotations.Test;

public class TestRefactoredInplaceInputShowcase extends AbstractWebDriverTest {

    @FindBy(className = "rf-ii")
    private RichFacesInplaceInput inplaceInput;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceInput/simple.xhtml");
    }

    @Test
    public void showcase1() {
        String expected = "RichFaces 4";
        inplaceInputAttributes.set(InplaceInputAttributes.showControls, true);
        inplaceInput.type(expected).cancelByControlls();
        assertEquals(inplaceInput.getTextInput().getStringValue(), expected);

        inplaceInput.type(expected).cancel();
        assertEquals(inplaceInput.getTextInput().getStringValue(), expected);

        expected = "foo bar";
        inplaceInput.type(expected).confirmByControlls();
        assertEquals(inplaceInput.getTextInput().getStringValue(), expected);

        inplaceInput.getTextInput().clear();

        inplaceInput.type(expected).confirm();
        assertEquals(inplaceInput.getTextInput().getStringValue(), expected);
    }
}
