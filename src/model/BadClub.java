package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class BadClub implements Serializable{

	BadSession session; 
	String clubID;
	ArrayList<BadSession> pastSessions = new ArrayList<>();
	ArrayList<BadPlayer> players = new ArrayList<>();
	LinkedList<BadPlayer> playerQueue = new LinkedList<>();
	//Each court along with what players it has, check against to see if need to decrement
	//Array of size number of courts
	int noCourts;
	int courtSize;
	int noCourtsUsed = 0;
		
	public ArrayList<BadPlayer> getPlayers() {
		return players;
	}

	public void freeCourt(){
	    noCourtsUsed--;
	    if(noCourtsUsed<0){
	        noCourtsUsed = 0;
        }
    }

    public void takeCourt(){
	    noCourtsUsed++;
    }

	public int getLowestElo(){
		int lowest = Integer.MAX_VALUE;
		int highest = Integer.MIN_VALUE;
		for(BadPlayer player : players){
			if(player.getElo() < lowest){
				lowest = player.getElo();
			}
			if(player.getElo() > highest){
				highest = player.getElo();
			}
		}

		return lowest;
	}

	public int getNoCourts(){
	    return noCourts;
    }

	public int getHighestElo(){
		int highest = Integer.MIN_VALUE;
		for(BadPlayer player : players){
			if(player.getElo() > highest){
				highest = player.getElo();
			}
		}

		return highest;
	}

	public void addPlayer(BadPlayer player)
	{
		players.add(player);
		playerQueue.add(player);


	}
	
	public void addPlayers(ArrayList<BadPlayer> players) {
		this.players.addAll(players);
	}

	public BadClub(String id, int noCourts, int courtSize) {
		this.clubID = id;
		this.noCourts = noCourts;
		this.courtSize = courtSize;
	}

	public int getCourtSize() {
		return courtSize;
	}

	public BadSession getSession() {
		return session;
	}

	public void setSession(BadSession session) {
		this.session = session;
	}

	public String getClubID() {
		return clubID;
	}

	public void setClubID(String clubID) {
		this.clubID = clubID;
	}
	
	public ArrayList<BadSession> getPastSessions(){
		return this.pastSessions;
	}
	
	public void newSession(BadSession sess) {
		pastSessions.add(this.session);
		this.session = sess;
	}

	public BadPlayer[] getTeam(int size){
		if(playerQueue.size() >= size && noCourtsUsed<noCourts) {
			BadPlayer[] players = new BadPlayer[size];
			players[0] = playerQueue.pop();

			takeCourt();

			PriorityQueue<BadPlayer> queue = new PriorityQueue<BadPlayer>(new Comparator<BadPlayer>() {
				@Override
				public int compare(BadPlayer o1, BadPlayer o2) {
					if(Math.abs(o1.getElo()-players[0].getElo()) < Math.abs(o2.getElo() - players[0].getElo())){
						return 1;
					}else if(Math.abs(o1.getElo()-players[0].getElo()) > Math.abs(o2.getElo() - players[0].getElo())){
						return -1;
					}
					return 0;
				}
			});

			queue.addAll(playerQueue);

			for(int i=1; i<players.length; i++) {
				players[i] = queue.poll();
				playerQueue.remove(players[i]);
			}
			return players;
		}
		else {
			return null;
		}

	}
	
	public void removeFromQueue(BadPlayer player) {
		playerQueue.remove(player);
	}
	
	public BadPlayer[] addToQueue(BadPlayer player) {
		playerQueue.add(player);
        return getTeam(this.courtSize);
	}
}
