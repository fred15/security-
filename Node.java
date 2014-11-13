import java.util.*;
import java.lang.*;

public class Node{
	String name;
	Node[] children = new Node[100];
	Node parent = null;
	int size = 0;
	String[][] roles = new String[100][2];

	public Node(String ascendent){
		name = ascendent;
	}

	public void add( String ascendent ){
		Node ascendentNode = new Node(ascendent);
		ascendentNode.parent = this;
		this.children[size] = ascendentNode;
		size++;
	}

	public String getName(){
		return name;
	}

	public String[][] getRoleArray(){
		return roles;
	}

	public ArrayList<String> getChildren(){
		if(size == 0){
			return null;
		}
		ArrayList<String> names = new ArrayList<String>();
		for(int i = 0; i<size;i++){
			names.add(children[i].getName());
		}
		return names;
	}

	public Node getParent(){
		return parent;
	}

	public Node findNode( String s ){

		if(name.equals(s)){
			return this;
		}else if( size > 0 ){
			for(int i = 0; i < size-1 ;i++){
				if((children[i].getName()).equals(s) ){
					return children[i];
				}
			}
			for(int i = 0; i < size;i++){
				Node results = children[i].findNode(s);
				if(results != null){
					return results;
				}
			}	
		}
		return null;
		
	}

	public Node getRoot(){
		if( this.getParent() != null){
			return (this.getParent()).getRoot();
		}	
		return this;
	}

	public String getRole(int i, int j){
		return roles[i][j];
	}

	public void addRole(String name, String role){
		for( int i = 0; i<100; i++){
			if( roles[i][0]== null){
				roles[i][0] = name;
				role = "\t"+role;
				roles[i][1] = role;
				if(parent != null){
					if( name.charAt(0) == 'R'){
						parent.addRole(name, "own,control");
					}
				}
				return;
			}else if( name.equals(roles[i][0])){
				String[] r = role.split(",");
				for(int j=0; j<r.length; j++){
					boolean trial = (roles[i][1]).contains(r[j]);
					if( trial == false ){
						roles[i][1] = roles[i][1]+","+r[j];
					}
				}	
				return;
			}
		}
		return;
	}

	public String findRole(String obj){
		for(int i=0; i<100;i++){
			if(obj.equals(roles[i][0])){ 
				return roles[i][1];
			}
		}
		return null;
	}

	public void addRoleToParent(String name, String role){
		for( int i = 0; i<100; i++){
			if( roles[i][0]== null){
				roles[i][0] = name;
				role = role;
				roles[i][1] = role;
				if(parent != null){
					parent.addRoleToParent(name,role);
				}
				return;
			}else if( name.equals(roles[i][0])){
				String[] r = role.split(",");
				for(int j=0; j<r.length; j++){
					r[0] = r[0].replace("\t","");
					boolean trial = (roles[i][1]).contains(r[j]);

					String s = "";
					s += r[j].charAt(0);

					boolean trial2 = (roles[i][1]).contains(s);
					if(trial2 && trial == false && (r[j]).charAt(1) == '*'){
						trial = true;
						System.out.println(s +" "+ roles[i][1]);
						roles[i][1] = roles[i][1].replace( s , s+"*");
						if(parent!=null){
							parent.addRoleToParent(name,role);
						}
					}
					if( trial == false ){
						roles[i][1] = roles[i][1]+","+r[j];					
						if(parent!=null){
							parent.addRoleToParent(name,role);
						}
					}
				}
				return;
			}
		}
		return;


	}

	public void printHierarchy(){
		if(children[0] == null){
				return;
		}
		System.out.print(this.getName()+"-->");
		for(int i = 0; i<size; i++){
			if(i==0){				
				System.out.print(children[i].getName());
			}else{
				System.out.print(", "+children[i].getName());
			}	
		}
		System.out.println();
		return;
	}//end printHierarchy
}	