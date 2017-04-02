/**
 * CS 472
 * Project_2: Cache Simulation
 * @author Donald Charest
 * 11/03/2016
 *
 */

public class Cache
{
	
	public int validBit;
	
    	/**
    	 * Method to get: validBit
    	 * @return validBit
    	 */
	    public int getValidBit() 
	    {
	            return validBit;
	    }
	    
	    /**
	     * Method to set: validBit
	     * @param validBit
	     */
	    public void setValidBit( int validBit ) 
	    {
	            this.validBit = validBit;
	    }
    
	    
    public int tag;
    
	    /**
		 * Method to get: tag
		 * @return tag
		 */
	    public int getTag() 
	    {
	    	return tag;
	    }
	    
	    /**
	     * Method to set: tag
	     * @param tag
	     */
	    public void setTag( int tag ) 
	    {
            this.tag = tag;
	    }

	    
    public int dirtyBit;
    
	    /**
		 * Method to get: dirtyBit
		 * @return dirtyBit
		 */
	    public int getDirtyBit() 
		{
            return dirtyBit;
	    }
	    
	    /**
	     * Method to set: dirtyBit
	     * @param dirtyBit
	     */
	    public void setDirtyBit( int dirtyBit )
	    {
            this.dirtyBit = dirtyBit;
	    }
        
	    
    public int[] dataBlock = new int[16];
    
	    /**
		 * Method to get: dataBlock
		 * @return dataBlock
		 */
	    public int[] getDataBlock() 
	    {
			return dataBlock;
		}
	    
	    /**
	     * Method to set: dataBlock
	     * @param dataBlock
	     */
	    public void setDataBlock( int[] dataBlock ) 
		{
			this.dataBlock = dataBlock;
		}
    
	    
    public int blockBeginAddress;
    
	    /**
		 * Method to get: blockBeginAddress
		 * @return blockBeginAddress
		 */
	    public int getBlockBeginAddress() 
	    {
			return blockBeginAddress;
		}  
	    
	    /**
	     * Method to set: blockBeginAddress
	     * @param blockBeginAddress
	     */
	    public void setBlockBeginAddress( int blockBeginAddress )
		{
			this.blockBeginAddress = blockBeginAddress;
		}

	    
} //End of Class: Cache
