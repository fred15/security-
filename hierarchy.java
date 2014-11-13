/*
*	Fred Montoya & Mikhail Batkalin
*	hierarchy.java
*	Intro to networks and security
*	Dr. Weiying Zhu
*/

import  java.io.*;
import java.lang.*;
import java.util.*;


public class hierarchy {

	public static String line = null;
	public static Scanner scaner = new Scanner(System.in);
	public static ArrayList<String> roles = new ArrayList<String>();
	public static String[] array = new String[0];
	public static Node root = new Node("");
	public static String[][] constrains = new String[20][30];
	public static String[][] usersRoles = new String[20][30];
	

	public static void main(String[] args) throws Exception{

		boolean fileRead = false;

		try{
			BufferedReader text;
			while( fileRead == false ){
				fileRead = true;
				int run = 0;
				text  = new BufferedReader(new FileReader("roleHierarchy.txt"));
				while((line = text.readLine())!=null){
					array = line.split("\\s+"); 


					if( run == 0){
						run++;
						root = new Node(array[1]);
						roles.add(array[1]);
					}
					if(fileRead && !roles.contains(array[1])){
						fileRead = false;
						run++;
						break;
					}else if(fileRead){
						for(int i = 0; i < 1; i++){
							if( !roles.contains(array[i])){

								roles.add(array[i]);
								root = root.getRoot();

								root = root.findNode(array[1]);
								root.add(array[0]);

							}else if(i == 0){	
								fileRead = false;
								break;
							}else{
								root = root.getRoot();
								root = root.findNode(array[1]);
								root.add(array[0]);	
							}
						}
						run++;
					}
				}
				root = root.getRoot();
				for(int i =0;i<roles.size();i++){
					root = root.findNode(roles.get(i));
					root.printHierarchy();
					root = root.getRoot();
				}


				if(fileRead == false){
					run--;
					System.out.println("invalid line is found in roleHierarchy.txt: line "+run+
						", enter any key to read it again");
					String s = scaner.next();
				}
			}	
			while(fileRead){
				text  = new BufferedReader(new FileReader("resourceObjects.txt"));
			    line = text.readLine();
				fileRead = false;
			    array = line.split("\\s+"); 
				fileRead = hasDuplicates(array);
			}		

			printMatrix();
			
			for(int i = 0;i< roles.size(); i++){
				root = root.getRoot();
				String name = roles.get(i);
				root = root.findNode(name);
				root.addRole(name, "control\t");
			}


		//ADDING ROLES THROUGH FILE

			text  = new BufferedReader(new FileReader("permissionsToRoles.txt"));
		    while((line = text.readLine())!=null){
		    	String[] array = line.split("\\s+");
				root = root.getRoot();
				root = root.findNode(array[0]);
				root.addRole(array[2], array[1]);
		    }


			for(int j=0; j<roles.size(); j++){
				for(int i=0; i< array.length; i++){
					root = root.getRoot();
					root = root.findNode(roles.get(j));
					String s = root.findRole(array[i]); 
					if( s != null && root.parent != null){
						(root.parent).addRoleToParent(array[i],s);
					}
				}	
			}



			printMatrix();

			int con = 0;	
			System.out.println();
	
			text  = new BufferedReader(new FileReader("roleSetsSSD.txt"));
		    while((line = text.readLine())!=null){
		    	constrains[con] = line.split("\\s+");
		    	System.out.print("Constrain "+(con+1)+", n = "+constrains[con][0]+", set of roles = {");
		    	for(int i = 0; i < constrains[con].length; i++){
	    			if(constrains[con][i] != null){
		    			if(i == 0){
							System.out.print(constrains[con][0]);
						}else{
		    				System.out.print(", "+constrains[con][i]);
		    			}
		    		}
		    	}
		    	System.out.println("}");
		    	con++;
		    }
		    System.out.println();

		    fileRead = true;
		    while(fileRead){
		    	fileRead = false;
			    con = 0;
				text  = new BufferedReader(new FileReader("usersRoles.txt"));
			    while((line = text.readLine())!=null){
			    	usersRoles[con] = line.split("\\s+");
			    	con++;
			    }
			    for(int i=0;i<usersRoles.length-1;i++){
			    	for(int j=(i+1); j< usersRoles.length;j++){
			    		if(usersRoles[i][0] != null && usersRoles[j][0] != null){
			    			if(usersRoles[i][0].equals(usersRoles[j][0])){
			    				System.out.println("invalid line is found in "+
									"usersRoles.txt: line "+i+" of the first invalid "+
									j +" due to duplicated "+usersRoles[i][0]+
									", enter any key to read it again:");
			    				fileRead = true;
			    				break;
			    			}
			    		}
			    	}
			    	if(fileRead){
			    		break;
			    	}

			    	for(int k = 0; k<usersRoles[i].length; k++){ 
			    		if(usersRoles[i][k] != null){
			    			for(int p = 0; p<constrains.length;p++ ){
			    				int number = 0;
			    				int n = 1000;
			    				if(constrains[p][0] != null){
			    					n = Integer.parseInt(constrains[p][0]);
			    				}
			    				for(int l = 1; l<constrains[p].length;l++ ){
			    					if(constrains[p][l] != null){
			    						if(usersRoles[i][k].equals(constrains[p][l])){
			    							number++;
			    							if(number > n){			
							    				System.out.println("invalid line is found in "+
													"usersRoles.txt: line "+i+" of the first invalid "+
													"due to constraint "+(p+1)+
													", enter any key to read it again:");
							    				fileRead = true;
							    				break;

			    							}
			    						}
			    					}
			    				}	
			    			}	
			    		}
			    	}
			    	if(fileRead){
			    		break;
			    	}
			    }
			    if(fileRead){
					String s = scaner.next();
				}
			}

			for(int i=0;i<roles.size();i++){
				System.out.print("\t"+roles.get(i));
			}
			System.out.println();

			for(int i=0;i<usersRoles.length;i++){
				if(usersRoles[i][0] != null){
					System.out.print(usersRoles[i][0]);
					int place = 0;
					int placeC = 0;
					for(int j=1;j<usersRoles[i].length;j++){
						for(int k=0;k<roles.size();k++){
							if(usersRoles[i][j].equals(roles.get(k))){
								System.out.print("\t+");
								place = k+1;
								placeC = place;
								break;
							}else if( placeC == 0) {
								System.out.print("\t");
							}else{
								placeC--;
							}	
						}	
					}
					System.out.println();
				}	
			}
			System.out.println();

		}catch(Exception e){
			e.printStackTrace();
		}

		boolean dothis = true;
		while(dothis){
			boolean match = false;
			boolean match2 = false;
			int p=0, k=0;
			dothis = false;		
			System.out.print("Please enter the user in your query: ");
			String u = scaner.nextLine();
			if( u.equals("")){
			}else{
				for(int i =0;i<usersRoles.length;i++){
		    		if(usersRoles[i][0] != null && usersRoles[i][0].equals(u)){
		    			match = true;
		    			p = i;
		    		}
			    }		
			}
			if( match == false){
				System.out.println("invalid user, try again.");
				dothis = true;
			}else{
				System.out.print("Please enter the object in your query (hit enter if it’s for any): ");
				String o = scaner.nextLine();
				if(o.equals("")){
					match2 = true;
				}else{	
					for(int i =1;i<usersRoles[k].length;i++){
						root = root.getRoot();
			    		if(usersRoles[k][i] != null){
							root = root.findNode(usersRoles[k][i]);
							String[][] objAcess = root.getRoleArray();
							for(int y = 0; y<objAcess.length;y++){
			    				if(objAcess[y][0] != null && objAcess[y][0].equals(o)){
			    					match2 = true;
								}
							}
			    		}

				    }
				}
				if( match2 == false){
					System.out.println("invalid object, try again.");
					dothis = true;
				}else {	
					String ar = "";				
					if(!o.equals("")){
						System.out.print("Please enter the access right in your query (hit enter if it’s for any): ");
						ar = scaner.nextLine();
						System.out.println();
					}
					if( o.equals("")){
						for(int i =1;i<usersRoles[k].length;i++){
							root = root.getRoot();
				    		if(usersRoles[k][i] != null){
								root = root.findNode(usersRoles[k][i]);
								String[][] objAcess = root.getRoleArray();
								for(int y = 0; y<objAcess.length;y++){
				    				if(objAcess[y][1] != null){
										System.out.println(objAcess[y][0]+"\t"+ objAcess[y][1]);
									}
								}
				    		}

					    }	

					}else if(ar.equals("")){
						for(int i =1;i<usersRoles[k].length;i++){
							root = root.getRoot();
				    		if(usersRoles[k][i] != null){
								root = root.findNode(usersRoles[k][i]);
								String[][] objAcess = root.getRoleArray();
								for(int y = 0; y<objAcess.length;y++){
				    				if(objAcess[y][1] != null && objAcess[y][0].equals(o)){
										System.out.println(objAcess[y][0]+"\t"+ objAcess[y][1]);
									}
								}
				    		}

					    }	


					}else{
						Boolean authorized = false;
						for(int i =1;i<usersRoles[k].length;i++){
							root = root.getRoot();
				    		if(usersRoles[k][i] != null){
								root = root.findNode(usersRoles[k][i]);
								String[][] objAcess = root.getRoleArray();
								for(int y = 0; y<objAcess.length;y++){
				    				if(objAcess[y][1] != null && objAcess[y][1].contains(ar)){
				    					authorized = true;
									}
								}
				    		}

					    }
					    if(authorized){
					    	System.out.println("authorized\n");
					    }else{					    	
					    	System.out.println("rejected\n");
					    }
					}
				}
			}

			if(dothis == false){
				System.out.print("Would you like to continue for the next query? ");
				String response = scaner.nextLine();
				if(response.equals("yes")){
					dothis = true;
				}
			}
		}	







	}//end of main	

	public static boolean hasDuplicates(String[] array ){
	    for (int i = 0; i < array.length - 1; ++i) {
	        for( int j = i+1; j<array.length;j++){
	        	if( array[i].equals(array[j])){
	        		System.out.println("duplicate object is found: "+array[i]+
	        						   ", enter any key to read it again");
					String s = scaner.next();
	        		return true;
	        	}
	        }
	    }
    	return false;
	}

	public static void printMatrix(){

		ArrayList<String> strings = (ArrayList<String>)roles.clone();
		ArrayList<String> rolesToFind = new ArrayList<String>();
		int times = 0;

		System.out.println("\n------------------------------------------"+
							 "------------------------------------------");
		for(int i = 0; i < array.length; i++){
			strings.add(array[i]);
		}
		for( int j=0;j<roles.size();j++){
			root = root.getRoot();
			root = root.findNode(roles.get(j));

			for(int i=0;i<strings.size();i++){
				if( (i+1)%5 == 0 ){
					System.out.println("\t\t"+strings.get(i));
					rolesToFind.add(strings.get(i));
					System.out.print(roles.get(j)+"\t");
					for(int k=0; k<rolesToFind.size();k++){
						for(int p=0; p<100; p++){
							String r = root.getRole(p,0);
							if( r != null && r.equals(rolesToFind.get(k))){
								String name = root.getRole(p,1);
								int thisround = times;
								int limit = k%4;
								if( limit==0 && k > 0){
									limit = 4;
								}
								for(int q = 0; q<limit;q++ ){
									if(thisround <= 0 ){
										System.out.print("\t\t");
									}
									thisround--;
									times++;
								}
								System.out.print(name);
								times++;
							}
						}
					}
					times = 0;
					rolesToFind.clear();
					System.out.println();
				}else{
					System.out.print("\t\t"+strings.get(i));
					rolesToFind.add(strings.get(i));
				}

			}
			System.out.print("\n"+roles.get(j)+"\t");
			for(int k=0; k<rolesToFind.size();k++){
				for(int p=0; p<100; p++){
					String r = root.getRole(p,0);
					if( r != null && r.equals(rolesToFind.get(k))){
						String name = root.getRole(p,1);
						int thisround = times;
						int limit = k%4;
						if( limit==0 && k > 0){
							limit = 4;
						}
						for(int q = 0; q<limit;q++ ){
							if(thisround <= 0 ){
								System.out.print("\t\t");
							}
							thisround--;
							times++;
						}
						System.out.print(name);
						if(name.length()<5){
							System.out.print("\t");
						}
						times++;
					}
				}
			}
			times = 0;
			rolesToFind.clear();
			System.out.println();
			System.out.println("------------------------------------------"+
							   "------------------------------------------");

		}	
		System.out.println();
	}


}