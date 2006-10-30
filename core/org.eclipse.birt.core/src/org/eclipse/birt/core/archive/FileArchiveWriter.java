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

package org.eclipse.birt.core.archive;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileArchiveWriter implements IDocArchiveWriter
{
	private String fileName;
	private String tempFolderName;
	private FolderArchiveWriter folderWriter; 			

	/**
	 * @param absolute fileName the archive file name
	 */
	public FileArchiveWriter( String fileName ) throws IOException
	{
		if ( fileName == null || fileName.length( ) == 0 )
			throw new IOException( "The file name is null or empty string." );

		File fd = new File( fileName );
		fileName = fd.getCanonicalPath( ); // make sure the file name is an
											// absolute path
		this.fileName = fileName;
		this.tempFolderName = fileName + ".tmpfolder";

		// try to create the parent folder
		File parentFile = new File( fileName ).getParentFile( );
		if ( parentFile != null && !parentFile.exists( ) )
		{
			parentFile.mkdirs( );
		}
		
		// try to create an empty file, if failed that means
		// some the file has been opened, throw out an exception.
		RandomAccessFile rf = new RandomAccessFile( fileName, "rw" );
		try
		{
			rf.setLength( 0 );
		}
		finally
		{
			rf.close( );
		}

		// try to remove the temp folder
		File archiveRootFolder = new File( tempFolderName );
		if ( archiveRootFolder.exists( ) )
		{
			ArchiveUtil.DeleteAllFiles( archiveRootFolder );
		}
		if ( archiveRootFolder.exists( ) )
		{
			throw new IOException( "archive root folder can't be removed" );
		}

		// Create archive folder
		archiveRootFolder.mkdirs( );
		folderWriter = new FolderArchiveWriter( tempFolderName );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#initialize()
	 */
	public void initialize() 
	{
		if (folderWriter != null)
		{
			folderWriter.initialize( );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#createRandomAccessStream(java.lang.String)
	 */
	public RAOutputStream createRandomAccessStream( String relativePath ) throws IOException
	{
		if ( folderWriter != null )
		{
			return folderWriter.createRandomAccessStream( relativePath );
		}
		throw new IOException( "the wrapped folder writer is null");
	}
	
	/**
	 * Delete a stream from the archive. NOTE: FileArchiveWriter doesn't support this function and always returns false; 
	 * @param relativePath - the relative path of the stream
	 * @return whether the operation was successful
	 * @throws IOException
	 */
	public boolean dropStream( String relativePath )
	{
		if (folderWriter != null)
		{
			return folderWriter.dropStream( relativePath );
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#getName()
	 */
	public String getName() 
	{
		return fileName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#exists()
	 */
	public boolean exists( String relativePath ) 
	{
		if (folderWriter != null)
		{
			return folderWriter.exists( relativePath );
		}
		return false;
	}	

	public void setStreamSorter( IStreamSorter streamSorter )
	{
		if (folderWriter != null)
		{
			folderWriter.setStreamSorter( streamSorter );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#finish()
	 */
	public void finish( ) throws IOException
	{
		if ( folderWriter != null )
		{
			// try to remove the temp folders
			RandomAccessFile rf = new RandomAccessFile( fileName, "rw" );
			try
			{
				folderWriter.finish( );
				folderWriter.toFileArchive( rf );
				folderWriter = null;
			}
			finally
			{
				rf.close( );
			}
			// try to lock the file
			ArchiveUtil.DeleteAllFiles( new File( tempFolderName ) );
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#flush()
	 */
	public void flush( ) throws IOException
	{
		if (folderWriter != null)
		{
			folderWriter.flush( );
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#lock(java.lang.String)
	 */
	public Object lock( String stream ) throws IOException
	{
//		if (folderWriter != null)
//		{
//			return folderWriter.lock( stream );
//		}
		//don't support progressive for file based document anymore.
		return stream.toString( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#unlock(java.lang.Object)
	 */
	public void unlock( Object lock )
	{
//		if (folderWriter != null)
//		{
//			folderWriter.unlock( lock );
//		}
	}
	
}