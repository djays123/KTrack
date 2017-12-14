package ktrack.ui.panels;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutoCompleteRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.StringAutoCompleteRenderer;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.HTML5Attributes;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;
import ktrack.entity.Dog;
import ktrack.repository.DogRepository;

public class CaregiverPanel extends Panel {
    /** The pattern for a phone number. */
    private static final String MOBILE_PATTERN = "(7|8|9)\\d{9}";
    
    /** The maximum number of elements returned to populate the auto complete dropdown. */
    private static final int MAX_AUTOCOMPLETE_ELEMENTS = 10;

    @SpringBean
    private DogRepository dogRepository;

    public CaregiverPanel(String id, boolean required, boolean autoComplete) {
        super(id);
        TextField<String> name = new TextField<String>("caregiver");
        add(name.setRequired(required).add(new HTML5Attributes()).setOutputMarkupId(true));
        TextField<String> mobile = new TextField<String>("caregiverMobile") {

            @Override
            protected String[] getInputTypes() {
                return new String[] { "tel" };
            }
        };
        mobile.setRequired(required).add(new HTML5Attributes()).setOutputMarkupId(true);
        mobile.add(new PatternValidator(MOBILE_PATTERN));
        mobile.setOutputMarkupId(true);
        EmailTextField email = (EmailTextField) new EmailTextField("caregiverEmail").setRequired(required)
                .add(new HTML5Attributes());
        
        if (autoComplete) {
            AutoCompleteSettings autoCompleteSettings = new AutoCompleteSettings();
            autoCompleteSettings.setMinInputLength(4);
            AutoCompleteBehavior<String> onTypeMobile = new AutoCompleteBehavior<String>(
                    StringAutoCompleteRenderer.INSTANCE, autoCompleteSettings) {
                @Override
                protected Iterator<String> getChoices(String input) {
                    return dogRepository.findDistinctMobile(input, MAX_AUTOCOMPLETE_ELEMENTS).iterator();
                }

            };

            mobile.add(onTypeMobile);
            AjaxFormSubmitBehavior onMobileChange = new AjaxFormSubmitBehavior("onchange") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    boolean isMobileEmpty = StringUtils.isEmpty(mobile.getRawInput());
                    boolean isNameEmpty = StringUtils.isEmpty(name.getRawInput());
                    boolean isEmailEmpty = StringUtils.isEmpty(email.getRawInput());
                    if (!isMobileEmpty && (isEmailEmpty || isNameEmpty)) {
                        update(target, dogRepository.findByMobile(mobile.getRawInput()), isNameEmpty ? name : null,
                                null, isEmailEmpty ? email : null);
                    }

                }
            };
            onMobileChange.setDefaultProcessing(false);
            mobile.add(onMobileChange);
        }
        add(mobile);
        
        email.add(EmailAddressValidator.getInstance()).setOutputMarkupId(true);
        if (autoComplete) {
            AutoCompleteSettings autoCompleteSettings = new AutoCompleteSettings();
            autoCompleteSettings.setMinInputLength(3);
            AutoCompleteBehavior<String> onTypeEmail = new AutoCompleteBehavior<String>(
                    StringAutoCompleteRenderer.INSTANCE, autoCompleteSettings) {
                @Override
                protected Iterator<String> getChoices(String input) {
                    return dogRepository.findDistinctEmail(input, MAX_AUTOCOMPLETE_ELEMENTS).iterator();
                }

            };

            email.add(onTypeEmail);
            AjaxFormSubmitBehavior onEmailChange = new AjaxFormSubmitBehavior("onchange") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    boolean isMobileEmpty = StringUtils.isEmpty(mobile.getRawInput());
                    boolean isNameEmpty = StringUtils.isEmpty(name.getRawInput());
                    boolean isEmailEmpty = StringUtils.isEmpty(email.getRawInput());
                    if (!isEmailEmpty && (isMobileEmpty || isNameEmpty)) {
                        update(target, dogRepository.findByEmail(email.getRawInput()), isNameEmpty ? name : null,
                                isMobileEmpty ? mobile : null, null);
                    }

                }
            };
            onEmailChange.setDefaultProcessing(false);
            email.add(onEmailChange);
        }
        add(email);

        add(new Icon("caregiver-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.user).build()));
        add(new Icon("caregiverMobile-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.mobile).build()));
        add(new Icon("caregiverEmail-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.envelope).build()));

    }

    private void update(AjaxRequestTarget target, Dog dog, TextField<String> name, TextField<String> mobile,
            TextField<String> email) {
        if (dog != null) {
            if (name != null) {
                name.setModelObject(dog.getCaregiver());
                target.add(name);
            }
            if (mobile != null) {
                mobile.setModelObject(dog.getCaregiverMobile());
                target.add(mobile);
            }
            if (email != null) {
                email.setModelObject(dog.getCaregiverEmail());
                target.add(email);
            }
        }
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

    /*    response.render(CssHeaderItem.forReference(new CssResourceReference(DefaultCssAutoCompleteTextField.class,
                "DefaultCssAutoCompleteTextField.css")));*/
        
        response.render(CssHeaderItem.forReference(new CssResourceReference(getClass(),
                "css/caregiverpanel.css")));
        
    }

    /**
     * A behavior that encapsulates the auto complete behavior for caregiver
     * controls.
     */
    private class CaregiverAutocompleteBahevior extends AutoCompleteBehavior<String> {

        public CaregiverAutocompleteBahevior(IAutoCompleteRenderer<String> renderer) {
            super(renderer);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected Iterator<String> getChoices(String input) {
            // TODO Auto-generated method stub
            return null;
        }

    }

}
