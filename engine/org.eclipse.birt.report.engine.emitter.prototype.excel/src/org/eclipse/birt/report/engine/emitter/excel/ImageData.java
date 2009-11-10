/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.emitter.excel.layout.XlsContainer;
import org.eclipse.birt.report.engine.layout.emitter.Image;
public class ImageData extends SheetData
{

	private String altText, imageUrl;
	private byte[] imageData;
	private Image imageInfo;
	private int width;

	public ImageData( IImageContent image, int styleId, int datatype,
			Image imageInfo, XlsContainer currentContainer )
	{
		super( );
		this.styleId = styleId;
		this.dataType = datatype;
		height = ExcelUtil.convertImageSize( image.getHeight( ),
				(int) ( imageInfo.getHeight( ) * ExcelUtil.PX_PT ) );
		int imageWidth = (int) ExcelUtil.convertImageSize( image.getWidth( ),
				(int) ( imageInfo.getWidth( ) * ExcelUtil.PX_PT ) );
		width = Math.min( currentContainer.getSizeInfo( ).getWidth( ),
				imageWidth );
		altText = image.getAltText( );
		imageUrl = image.getURI( );
		this.imageData = imageInfo.getData( );
		rowSpanInDesign = 0;
		this.imageInfo = imageInfo;
	}

	public String getDescription( )
	{
		return altText;
	}

	public void setDescription( String description )
	{
		this.altText = description;
	}

	public String getImageUrl( )
	{
		return imageUrl;
	}

	public void setUrl( String url )
	{
		this.imageUrl = url;
	}

	public byte[] getImageData( )
	{
		return imageData;
	}

	public void setImageData( byte[] imageData )
	{
		this.imageData = imageData;
	}

	public Image getImageInfo( )
	{
		return imageInfo;
	}

	public int getWidth( )
	{
		return width;
	}

	public void setWidth( int width )
	{
		this.width = width;
	}
}
