package ktrack.ui.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal.Size;
import ktrack.entity.Dog;

public class BookingPreview<T> extends Modal<T> {

    public BookingPreview(String markupId) {
        super(markupId);
        
        Dog dog = new Dog();
          
        CompoundPropertyModel<Dog> dogModel = new CompoundPropertyModel<Dog>(Model.of(dog));
        Form<Dog> bookingPreviewForm = new Form<Dog>("slot-booking-form", dogModel);
        bookingPreviewForm.add(new CaregiverPanel("caregiverPanel", true).setRenderBodyOnly(true));
        bookingPreviewForm.add(new KennelPanel("kennelPanel").setRenderBodyOnly(true));

        final Model<String> bookingDate = new Model<String>("");
        HiddenField<String> bookingDateHolder = new HiddenField<String>("slot-booking-date", bookingDate, String.class);
        bookingPreviewForm.add(bookingDateHolder);

        AjaxFormSubmitBehavior ajaxFormSubmitBehavior = new AjaxFormSubmitBehavior("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
            
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);
                attributes.setPreventDefault(true);
            }
        };

        bookingPreviewForm.add(ajaxFormSubmitBehavior);
        add(bookingPreviewForm);
        size(Size.Large);
    }

}
