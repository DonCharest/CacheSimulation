/**
 * CS 472
 * Project_2: Cache Simulation
 * @author Donald Charest
 * 11/03/2016
 *
 */

import java.util.Scanner;

public class CacheSimulation 
{
	public static String input;
	public static int address, value, tag, slot, blockBeginAddress, cacheSize = 16;
	public static Cache[] cache = new Cache[16];
	public static int[] MM = new int[2048];

	static Scanner keyboard = new Scanner( System.in );

	/**
	 * Main Method
	 * @param args
	 * @param Initialize
	 * @param DisplayMenuOptions
	 */
	public static void main( String[] args ) 
	{
		Initialize();
		
		DisplayMenuOptions();
		
	} //End of Main Method
	
	
	/**
	 * Method to Initialize & Zero out Main_Memory & Cache
	 * @param MM (Main_Memory) 2K
	 * @param Cache 16 slots; 16 bits/slot
	 */
	public static void Initialize()
	{
		//Initialize MM; Set all to Zero
		for ( int i = 0x0; i <= 0x7FF; i++ )
		{
			MM[i] = ( i & 0x0FF );  
		}
		//Initialize Cache; Set all to Zero
		for (int j = 0; j < cache.length; j++) 
		{
			cache[j] = new Cache();
			cache[j].setValidBit( 0 );
			cache[j].setTag( 0 );
			cache[j].setDirtyBit( 0 );
		}
		
	} //End of method - Initialize() 
	
	
	/**
	 * Method to display valid user options - this method loops until program is quit
	 * @param read address: address value stored at
	 * @param write address: address to write value to
	 * @param display cache: current cache store
	 */
	public static void DisplayMenuOptions() 
	{
		System.out.println( "\n(R)ead, (W)rite, or (D)isplay Cache?\n" );
		input = keyboard.next();
		
		if ( input.equals( "R" ) ) 
		{
			ReadCache();
		}
		else if ( input.equals( "W" ) ) 
		{
			WriteToCache();
		}
		else if ( input.equals( "D" ) ) 
		{
			DisplayCache();
		}
		else 
		{
			System.out.println(" Option not recognized, please choose a valid menu option" );
			
			DisplayMenuOptions();
		}
		
	} //End of method - DisplayMenuOptions() 
	
	
	/**
	 * Method to read user specified address in cache
	 * @param blockBeginAddress - block always begins at "0x0" & ends at "0xF"
	 * @param slot - ( 0 - 16 ) available cache slots
	 * @param valid - ( 0 or 1 ) valid = "1"; if valid check Tag; if false slot is empty -> "Cache Miss"
	 * @param tag - hexadecimal value must be ( <= 0x7 ); Tag matches -> "Cache Hit"
	 * @param dirty Bit - ( 0 or 1 ) dirty = "1"; if dirty data in cache not saved in Main_Memory
	 */
	public static void ReadCache() 
	{
		EnterAddressToRead();

		blockBeginAddress = ( address & 0xFF0 );
		tag = ( address >> 8 );
		slot = ( (address & 0x0F0 ) >> 4 );

		// Valid bit "0"; Cache Miss - copy 16-bit data block from Main_Memory, set valid bit to "1" & update tag
		if ( cache[slot].getValidBit() == 0 )
		{       	    
			cache[slot].setValidBit( 1 );
			cache[slot].setTag( tag );
			cache[slot].setBlockBeginAddress( blockBeginAddress );
			
			System.arraycopy( MM, cache[slot].getBlockBeginAddress(), cache[slot].dataBlock, 0, cacheSize );
			System.out.printf( "At that byte there is the value " + Integer.toHexString( MM[address] ).toUpperCase() + " ( Cache Miss )\n" );
		}
		//Valid bit "1"; Tag not a match -> Cache Miss - Cache Miss - copy 16-bit data block from Main_Memory, set valid bit to "1" & update tag
		else if ( cache[slot].getValidBit() == 1 && cache[slot].getTag() != tag ) 
		{
			System.out.printf( "At that byte there is the value " + Integer.toHexString( MM[address] ).toUpperCase() + " ( Cache Miss )\n" );
			
			if (cache[slot].getDirtyBit() == 1 ) 
			{	
				//copy contents of slot into main memory before loading new block
				System.arraycopy( cache[slot].dataBlock, 0, MM,cache[slot].getBlockBeginAddress(), cacheSize );
				cache[slot].setDirtyBit( 0 );
			}
			//set up new block
			blockBeginAddress = ( address & 0xFF0 );
			cache[slot].setTag( tag );
			cache[slot].setBlockBeginAddress( blockBeginAddress );
			System.arraycopy( MM, cache[slot].getBlockBeginAddress(), cache[slot].dataBlock, 0, cacheSize );
		}
		// Valid bit "1"; tag matches -> cache Hit
		else if ( cache[slot].getValidBit() == 1 && tag == cache[slot].getTag() ) 
		{
			System.out.printf( "At that byte there is the value " + Integer.toHexString( MM[address] ).toUpperCase() + " (Cache Hit)\n" );
		}
		
		DisplayMenuOptions();
	} //End of method - ReadToCache() 

	
	/**
	 * Method to write to user specified value into cache
	 * @param blockBeginAddress - block always begins at "0x0" & ends at "0xF"
	 * @param slot - ( 0 - 16 ) available cache slots
	 * @param valid - ( 0 or 1 ) valid = "1"; if valid check Tag; if false slot is empty -> "Cache Miss"
	 * @param tag - hexadecimal value must be ( <= 0x7 ); if Tag matches -> "Cache Hit"
	 * @param dirty Bit - ( 0 or 1 ) dirty = "1"; if dirty data in cache not saved in Main_Memory
	 */
	public static void WriteToCache() 
	{
		EnterAddressToWrite();
		
		EnterValue();
		
		blockBeginAddress = ( address & 0xFF0 );
		tag = ( address >> 8 );
		slot = ( ( address & 0x0F0 ) >> 4 );
		
		// Valid bit "0"; Cache Miss - copy 16-bit data block from Main_Memory, update bit with user specified value, set valid bit to "1" & update tag
		if (cache[slot].getValidBit() == 0) 
		{
			System.out.printf( "Value " + Integer.toHexString( value ).toUpperCase() + " has been written to address %X. (Cache Miss)\n", 0xFFF & address );

			cache[slot].setValidBit( 1 );
			cache[slot].setTag( tag );
			cache[slot].setDirtyBit( 1 );
			cache[slot].setBlockBeginAddress( blockBeginAddress );
			MM[address] = value;

			System.arraycopy( MM, cache[slot].getBlockBeginAddress(), cache[slot].dataBlock, 0, cacheSize );
			
			for ( int i = 0; i <= 15; i++ ) 
			{
				if ( cache[slot].dataBlock[i] == ( 0x00F & address ) ) 
				{
					cache[slot].dataBlock[i] = value;
					
					cache[slot].setDirtyBit( 1 );
				}
			}
		}
		
		// Valid bit "1"; Tag not a match; Cache Miss - check if dirty bit "1"
		else if ( cache[slot].getValidBit() == 1 && cache[slot].getTag() != tag ) 
		{
			// If dirty bit "1" copy 16-bit data block from Main_Memory before writing new value to cache
			if ( cache[slot].getDirtyBit() == 1 )
			{
				System.arraycopy( cache[slot].dataBlock, 0, MM,cache[slot].getBlockBeginAddress(), cacheSize );
			}
			
			System.out.printf( "Value " + Integer.toHexString(value).toUpperCase()  + " has been written to address %X. (Cache Miss)\n", 0xFFF & address );
			
			// Update tag & set valid bit & dirty bit to "1"
			cache[slot].setValidBit( 1);
			cache[slot].setTag(tag);
			cache[slot].setDirtyBit( 1);
			cache[slot].setBlockBeginAddress(blockBeginAddress);
			
			System.arraycopy( MM, cache[slot].getBlockBeginAddress(), cache[slot].dataBlock, 0, cacheSize );
			
			// Update data block in cache to include the new value
			for ( int i = 0; i < 16; i++ ) 
			{
				if ( i == ( 0x00F & address ) ) 
				{
					cache[slot].dataBlock[i] = value;	
				}
			}
		}
		// Valid bit "1"; tag matches -> Cache Hit - update value, set dirty bit to "1"
		else if ( cache[slot].getValidBit() != 0 && cache[slot].getTag() == tag ) 
		{
			System.out.printf( "Value " + Integer.toHexString(value).toUpperCase()  + " has been written to address %X. (Cache Hit)\n", 0xFFF & address );
			for ( int i = 0; i < 16; i++ ) 
			{
				if ( i == ( 0x00F & address ) ) 
				{	 
					cache[slot].dataBlock[i] = value;
					
					cache[slot].setDirtyBit( 1 );
				}
			}
		}
		
		DisplayMenuOptions();
	} //End of method - WriteToCache() 
	
	
	/**
	 * Method to display what is currently stored in cache
	 * @param slot - ( ( address & 0x00f0 ) >> 4 ); ( 0 - 16 ) available cache slots
	 * @param valid - ( 0 or 1 ) valid = "1"
	 * @param tag - ( address >> 8 ); hexadecimal value must be ( <= 0x7 )
	 */
	public static void DisplayCache() 
	{
		System.out.println( "Slot    Valid   Tag     Data" );
		for ( int i = 0; i < 16; i++ ) 
		{
			System.out.printf( Integer.toHexString(i).toUpperCase() + "\t" + cache[i].getValidBit() + "\t" + "%X\t", cache[i].getTag() );
			for ( int j = 0; j < cacheSize; j++ ) 
			{
				System.out.printf( "%X ", cache[i].dataBlock[j] );
			}
			System.out.println();
		}
		DisplayMenuOptions();
		
	} //End of method - DisplayCache()  
	
		
	/** 
	 * Method to choose cache address to read
	 * @param hexadecimal value must be ( <= 0x7FF ) 
	 */ 
	public static void EnterAddressToRead()
	{
		System.out.println( "What address would you like read? " );
		address = keyboard.nextInt( 16 );	
		
		if ( address > 0x7FF )
		{
			System.out.println( "Please entern a hexadecimal address no larger than 0x7FF" );
			
			EnterAddressToRead();
		}
		else 
		{
			//Continue
		}
		
	} //End of method - EnterAddressToRead() 
	
	
	/** 
	 * Method to choose cache address to write to
	 * @param address -  must be hexadecimal <= 0x7FF
	 */ 
	public static void EnterAddressToWrite()
	{
		System.out.println( "What address would you like to write to? " );
		address = keyboard.nextInt( 16 );
		
		if ( address > 0x7FF )
		{
			System.out.println( "Please enter a hexadecimal address no larger than 0x7FF" );
			
			EnterAddressToWrite();
		}
		else 
		{
			//Continue
		}
		
	} //End of method - EnterAddressToWrite() 
	
	
	/** 
	 * Method to enter value to save at specified address
	 * @param value - must be hexadecimal <= 0xFF 
	 */ 
	public static void EnterValue()
	{
		System.out.println( "And what value do you want to write to it? " );
		value = keyboard.nextInt( 16 );
		
		if ( value > 0xFF )
		{
			System.out.println( "Please enter a hexadecimal value no larger than 0xFF" );
			
			EnterValue();
		}
		else
		{
			//Continue
		}
		
	} //End of method - EnterValue() 
	
	
} //End of Class - CacheSimulation