/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.page;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SimpleCubeBuilder extends TitleAreaDialog
{

	public SimpleCubeBuilder( Shell parentShell )
	{
		super( parentShell );
	}

	private CubeHandle cube;
	private DataSetHandle dataset;
	private Text nameText;
	private CubeGroupContent group;

	public void setInput( CubeHandle cube, DataSetHandle dataset )
	{
		this.cube = cube;
		this.dataset = dataset;
	};

	protected Control createDialogArea( Composite parent )
	{
		Composite area = (Composite) super.createDialogArea( parent );
		Composite container = new Composite( area, SWT.NONE );
		container.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		container.setLayout( new GridLayout( ) );

		this.setTitle( Messages.getString( "SimpleCubeBuilder.Title.Title" ) );
		this.setMessage( Messages.getString( "SimpleCubeBuilder.Title.Message" ) );

		createNameArea( container );
		group = new CubeGroupContent( container, SWT.NONE );
		group.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		initDialog( );
		return area;
	}

	private void initDialog( )
	{
		if ( cube != null )
		{
			nameText.setText( cube.getName( ) == null ? "" : cube.getName( ) );
			group.setInput( cube, dataset );
			group.load( );
		}

	}

	protected void createNameArea( Composite parent )
	{

		Composite nameArea = new Composite( parent, SWT.NONE );
		nameArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		GridLayout layout = new GridLayout( 3, false );
		layout.marginWidth = 10;
		nameArea.setLayout( layout );

		Label nameLabel = new Label( nameArea, SWT.NONE );
		nameLabel.setText( Messages.getString( "SimpleCubeBuilder.Label.Name" ) );

		nameText = new Text( nameArea, SWT.BORDER );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.grabExcessHorizontalSpace = true;
		nameText.setLayoutData( gd );
		nameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				Button finishButton = getButton( IDialogConstants.OK_ID );
				if ( !nameText.getText( ).trim( ).equals( "" ) )
				{
					String name = nameText.getText( ).trim( );
					try
					{
						cube.setName( name );
						if ( finishButton != null )
							finishButton.setEnabled( true );
						SimpleCubeBuilder.this.setErrorMessage( null );
						SimpleCubeBuilder.this.setMessage( Messages.getString( "SimpleCubeBuilder.Title.Message" ) );
					}
					catch ( NameException e1 )
					{
						SimpleCubeBuilder.this.setErrorMessage( e1.getMessage( ) );
						if ( finishButton != null )
							finishButton.setEnabled( false );
					}
					group.refresh( );

				}
				else
				{
					if ( finishButton != null )
						finishButton.setEnabled( false );
				}
			}

		} );

		Label space = new Label( nameArea, SWT.NONE );
		gd = new GridData( );
		gd.widthHint = 70;
		space.setLayoutData( gd );

	}

	protected void createButtonsForButtonBar( Composite parent )
	{
		super.createButtonsForButtonBar( parent );
		getButton( IDialogConstants.OK_ID ).setText( IDialogConstants.FINISH_LABEL );
	}

	protected void configureShell( Shell shell )
	{
		super.configureShell( shell );
		shell.setText( Messages.getString( "SimpleCubeBuilder.Title" ) );
	}

}
