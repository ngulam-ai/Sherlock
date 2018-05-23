package agency.akcom.mmg.sherlock.ui.client.widget;

import org.gwtbootstrap3.client.ui.constants.ImageType;

import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class MyImageCell extends ImageCell {

	interface Template extends SafeHtmlTemplates {
		@Template("<img src=\"{0}\" />")
		SafeHtml img(String url);

		@Template("<img src=\"{0}\"  class=\"{1}\" />")
		SafeHtml img(String url, String classStr);
	}

	private static Template template;
	private ImageType type = ImageType.DEFAULT;

	public MyImageCell() {
		if (template == null) {
			template = GWT.create(Template.class);
		}
	}

	public MyImageCell(ImageType type) {
		this();

		this.type = type;
	}

	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		if (value != null) {
			// The template will sanitize the URI.
			if (type.equals(ImageType.DEFAULT))
				sb.append(template.img(value));
			else
				sb.append(template.img(value, type.getCssName()));
		}
	}

}
