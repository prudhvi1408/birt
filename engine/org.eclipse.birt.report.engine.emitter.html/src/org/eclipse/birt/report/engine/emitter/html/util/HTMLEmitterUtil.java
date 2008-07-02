/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.html.util;

import java.util.List;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.html.HTMLTags;
import org.eclipse.birt.report.engine.emitter.html.HTMLWriter;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Utility class for html emitter.
 *
 */
public class HTMLEmitterUtil
{
	/**
	 * display type of Block
	 */
	public static final int DISPLAY_BLOCK = 1;

	/**
	 * display flag which contains all display types
	 */
	public static final int DISPLAY_FLAG_ALL = 0xffff;

	/**
	 * display type of Inline
	 */
	public static final int DISPLAY_INLINE = 2;

	/**
	 * display type of Inline-Block
	 */
	public static final int DISPLAY_INLINE_BLOCK = 4;

	/**
	 * display type of none
	 */
	public static final int DISPLAY_NONE = 8;

	public static int getElementType( IContent content )
	{
		return getElementType( content.getX( ), content.getY( ), content
				.getWidth( ), content.getHeight( ), content.getStyle( ) );
	}

	public static String getTagByType( int display, int mask )
	{
		int flag = display & mask;
		String tag = null;
		if ( ( flag & DISPLAY_BLOCK ) > 0 )
		{
			tag = HTMLTags.TAG_DIV;
		}

		if ( ( flag & DISPLAY_INLINE ) > 0 )
		{
			tag = HTMLTags.TAG_SPAN;
		}

		return tag;
	}

	/**
	 * Add element to active ID list. Output type & iid to html.
	 * 
	 * @param content
	 *            the styled element content
	 */
	public static void setActiveIDTypeIID( HTMLWriter writer, List ouputInstanceIDs,
			String htmlIDNamespace, IContent content )
	{
		// If content is generated by LabelItemDesign or TemplateDesign,
		// ExtendedItemDesign, TableItemDesign
		// add it to active id list, and output type & iid to html
		String type = getActiveIdType( content );
		if ( type != null )
		{
			// Instance ID
			InstanceID iid = content.getInstanceID( );
			long componentID = ( iid != null ) ? iid.getComponentID( ) : 0;
			setActiveIDTypeIID( writer, ouputInstanceIDs,
					htmlIDNamespace, content.getBookmark( ), type, iid, componentID );
		}
	}

	public static void setActiveIDTypeIID( HTMLWriter writer,
			List ouputInstanceIDs, String htmlIDNamespace, String bookmark,
			String type, InstanceID iid, long elementId )
	{
		String htmlBookmark;
		if ( null != htmlIDNamespace )
		{
			htmlBookmark = htmlIDNamespace + bookmark;
		}
		else
		{
			htmlBookmark = bookmark;
		}
		exportElementID( ouputInstanceIDs, htmlBookmark, type, elementId );

		
		// type
		writer.attribute( "element_type", type );
		if ( iid != null )
		{
			if ( type != TYPE_LABEL
					&& type != TYPE_TEMPLATE && type != TYPE_DATA
					&& type != TYPE_TEXT && type != TYPE_UNKNOWN )
			{
				writer.attribute( "iid", iid.toUniqueString( ) );
			}
			else
			{
				writer.attribute( "iid", iid.toString( ) );
			}
		}
	}

	/**
	 * Outputs the 'bookmark' property. Destination anchors in HTML documents
	 * may be specified either by the A element (naming it with the 'name'
	 * attribute), or by any other elements (naming with the 'id' attribute).
	 * 
	 * @param tagName
	 *            The tag's name.
	 * @param bookmark
	 *            The bookmark value.
	 */
	public static void setBookmark( HTMLWriter writer, String tagName, String htmlIDNamespace, String bookmark )
	{
		String htmlBookmark;
		if ( null != htmlIDNamespace
				&& null != bookmark && bookmark.length( ) > 0 )
		{
			htmlBookmark = htmlIDNamespace + bookmark;
		}
		else
		{
			htmlBookmark = bookmark;
		}
		
		if ( tagName == null || !HTMLTags.TAG_A.equalsIgnoreCase( tagName ) )
		{
			writer.attribute( HTMLTags.ATTR_ID, htmlBookmark );
		}
		else
		{
			writer.attribute( HTMLTags.ATTR_ID, htmlBookmark );
			writer.attribute( HTMLTags.ATTR_NAME, htmlBookmark );
		}
	}

	private static void exportElementID( List ouputInstanceIDs, String bookmark,
			String type, long componentID )
	{
		if ( ouputInstanceIDs != null )
		{
			if ( bookmark != null )
			{
				assert type != null;
				StringBuffer buffer = new StringBuffer();
				buffer.append(bookmark);
				buffer.append(",");
				buffer.append(type);
				buffer.append(",");
				buffer.append(componentID);
				ouputInstanceIDs.add( buffer.toString() );
			}
		}
	}

	static final String TYPE_LABEL = "LABEL";
	static final String TYPE_TEMPLATE = "TEMPLATE";
	static final String TYPE_EXTENDED = "EXTENDED";
	static final String TYPE_TABLE = "TABLE";
	static final String TYPE_LIST = "LIST";
	static final String TYPE_DATA = "DATA";
	static final String TYPE_TEXT = "TEXT";
	static final String TYPE_UNKNOWN = null;

	private static String getActiveIdType( IContent content )
	{
		Object genBy = content.getGenerateBy( );
		if ( genBy instanceof LabelItemDesign )
		{
			return TYPE_LABEL;
		}
		if ( genBy instanceof TemplateDesign )
		{
			return TYPE_TEMPLATE;
		}

		if ( genBy instanceof ExtendedItemDesign )
		{
			DesignElementHandle handle = ( (ExtendedItemDesign) genBy ).getHandle( );
			if ( handle instanceof ExtendedItemHandle )
			{
				return ( (ExtendedItemHandle) handle ).getExtensionName( );
			}
			return TYPE_EXTENDED;
		}
		if ( genBy instanceof TableItemDesign )
		{
			return TYPE_TABLE;
		}
		if ( genBy instanceof ListItemDesign )
		{
			return TYPE_LIST;
		}
		if ( genBy instanceof DataItemDesign )
		{
			return TYPE_DATA;
		}
		if ( genBy instanceof TextItemDesign )
		{
			return TYPE_TEXT;
		}
		return TYPE_UNKNOWN;
	}
	
	private static int getElementType( DimensionType x, DimensionType y,
			DimensionType width, DimensionType height, IStyle style )
	{
		int type = 0;
		String display = null;
		if ( style != null )
		{
			display = style.getDisplay( );
		}

		if ( EngineIRConstants.DISPLAY_NONE.equalsIgnoreCase( display ) )
		{
			type |= DISPLAY_NONE;
		}
		if ( x != null || y != null )
		{
			return type | DISPLAY_BLOCK;
		}
		else if ( EngineIRConstants.DISPLAY_INLINE.equalsIgnoreCase( display ) )
		{
			type |= DISPLAY_INLINE;
			if ( width != null || height != null )
			{
				type |= DISPLAY_INLINE_BLOCK;
			}
			return type;
		}
		return type | DISPLAY_BLOCK;
	}
}
