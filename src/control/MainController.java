package control;

import model.Edge;
import model.Graph;
import model.List;
import model.Vertex;

import java.time.temporal.ValueRange;

/**
 * Created by Jean-Pierre on 12.01.2017.
 */
public class MainController {

    //Attribute

    //Referenzen
    private Graph allUsers;

    public MainController(){
        allUsers = new Graph();
        createSomeUsers();
    }

    /**
     * Fügt Personen dem sozialen Netzwerk hinzu.
     */
    private void createSomeUsers(){
        insertUser("Ulf");
        insertUser("Silent Bob");
        insertUser("Dörte");
        insertUser("Ralle");
        befriend("Silent Bob", "Ralle");
        befriend("Dörte", "Ralle");
    }

    /**
     * Fügt einen Nutzer hinzu, falls dieser noch nicht existiert.
     * @param name
     * @return true, falls ein neuer Nutzer hinzugefügt wurde, sonst false.
     */
    public boolean insertUser(String name){
        //TODO 05: Nutzer dem sozialen Netzwerk hinzufügen.
        if (allUsers.getVertex(name) == null && name != null && !name.isEmpty()){
            allUsers.addVertex(new Vertex(name));
            return true;
        }
        return false;
    }

    /**
     * Löscht einen Nutzer, falls dieser existiert. Alle Verbindungen zu anderen Nutzern werden ebenfalls gelöscht.
     * @param name
     * @return true, falls ein Nutzer gelöscht wurde, sonst false.
     */
    public boolean deleteUser(String name){
        //TODO 07: Nutzer aus dem sozialen Netzwerk entfernen.
        if (allUsers.getVertex(name) != null && name!= null && !name.isEmpty()){
            allUsers.removeVertex(allUsers.getVertex(name));
            return true;
        }
        return false;
    }

    /**
     * Falls Nutzer vorhanden sind, so werden ihre Namen in einem String-Array gespeichert und zurückgegeben. Ansonsten wird null zurückgegeben.
     * @return
     */
    public String[] getAllUsers(){
        //TODO 06: String-Array mit allen Nutzernamen erstellen.
        if (allUsers == null || allUsers.isEmpty()) return null;
        List<Vertex> list = allUsers.getVertices();
        int counter = 0;
        if (!list.isEmpty()) {
            while (list.hasAccess()) {
                counter++;
                list.next();
            }
            if (counter > 0) {
                list.toFirst();
                String[] names = new String[counter];
                for (int i = 0; i < counter; i++) {
                    names[i] = list.getContent().getID();
                    list.next();
                }
                return names;
            }
        }
        return null;
    }

    /**
     * Falls der Nutzer vorhanden ist und Freunde hat, so werden deren Namen in einem String-Array gespeichert und zurückgegeben. Ansonsten wird null zurückgegeben.
     * @param name
     * @return
     */
    public String[] getAllFriendsFromUser(String name){
        //TODO 09: Freundesliste eines Nutzers als String-Array erstellen.
        Vertex v = allUsers.getVertex(name);
        if (v == null) return null;

        List<Vertex> friends = allUsers.getNeighbours(v);
        if (friends.isEmpty()) return null;
        friends.toFirst();
        int counter = 0;
        while (friends.hasAccess()){
            counter++;
            friends.next();
        }
        String[] array = new String[counter];
        friends.toFirst();
        for (int i = 0; i < counter; i++){
            array[i] = friends.getContent().getID();
            friends.next();
        }
        return array;
    }

    /**
     * Bestimmt den Zentralitätsgrad einer Person im sozialen Netzwerk, falls sie vorhanden ist. Sonst wird -1.0 zurückgegeben.
     * Der Zentralitätsgrad ist der Quotient aus der Anzahl der Freunde der Person und der um die Person selbst verminderten Anzahl an Nutzern im Netzwerk.
     * Gibt also den Prozentwert an Personen im sozialen Netzwerk an, mit der die Person befreundet ist.
     * @param name
     * @return
     */
    public double centralityDegreeOfUser(String name){
        //TODO 10: Prozentsatz der vorhandenen Freundschaften eines Nutzers von allen möglichen Freundschaften des Nutzers.
        Vertex v = allUsers.getVertex(name);
        if (v == null) return -1.0;
        double userAmount = -1.0;
        double friendAmount = 0.0;

        List<Vertex> list = allUsers.getVertices();
        while (list.hasAccess()){
            userAmount++;
            list.next();
        }

        List<Vertex> friends = allUsers.getNeighbours(v);
        friends.toFirst();
        while (friends.hasAccess()){
            friendAmount++;
            friends.next();
        }

        if (userAmount == 0) return -1.0;
        return friendAmount / userAmount;
    }

    /**
     * Zwei Nutzer des Netzwerkes gehen eine Freundschaft neu ein, falls sie sich im Netzwerk befinden und noch keine Freunde sind.
     * @param name01
     * @param name02
     * @return true, falls eine neue Freundeschaft entstanden ist, ansonsten false.
     */
    public boolean befriend(String name01, String name02){
        //TODO 08: Freundschaften schließen.
        Vertex v1 = allUsers.getVertex(name01);
        Vertex v2 = allUsers.getVertex(name02);
        if (v1 != null && v2 != null && allUsers.getEdge(v1, v2) == null){
            Edge sheeran = new Edge(v1, v2, 1);
            allUsers.addEdge(sheeran);
            return true;
        }
        return false;

        /* Kompliziert
        if (allUsers.getVertex(name01) != null && allUsers.getVertex(name02) != null && !name01.equals(name02)) {
            List<Vertex> list = allUsers.getNeighbours(allUsers.getVertex(name01));
            list.toFirst();
            while (list.hasAccess()) {
                if (list.getContent() == allUsers.getVertex(name02)) return false;
                list.next();
            }
            allUsers.addEdge(new Edge(allUsers.getVertex(name01), allUsers.getVertex(name02), 1));
            return true;
        }
        return false;
        */
    }

    /**
     * Zwei Nutzer beenden ihre Freundschaft, falls sie sich im Netzwerk befinden und sie befreundet sind.
     * @param name01
     * @param name02
     * @return true, falls ihre Freundschaft beendet wurde, ansonsten false.
     */
    public boolean unfriend(String name01, String name02){
        //TODO 11: Freundschaften beenden.
        Vertex v1 = allUsers.getVertex(name01);
        Vertex v2 = allUsers.getVertex(name02);
        if (v1 != null && v2 != null && !name01.equals(name02)){
            if (allUsers.getEdge(v1, v2) != null){
                allUsers.removeEdge(allUsers.getEdge(v1, v2));
                return true;
            }
        }
        return false;
    }

    /**
     * Bestimmt die Dichte des sozialen Netzwerks und gibt diese zurück.
     * Die Dichte ist der Quotient aus der Anzahl aller vorhandenen Freundschaftsbeziehungen und der Anzahl der maximal möglichen Freundschaftsbeziehungen.
     * @return
     */
    public double dense(){
        //TODO 12: Dichte berechnen.
        List<Vertex> users = allUsers.getVertices();
        allUsers.setAllEdgeMarks(true);
        double friendships = 0.0;
        double counter = 0.0;
        while (users.hasAccess()){
            List<Edge> userEdges = allUsers.getEdges(users.getContent());
            userEdges.toFirst();
            while (userEdges.hasAccess()){
                if (userEdges.getContent().isMarked()){
                    userEdges.getContent().setMark(false);
                    friendships++;
                }
                userEdges.next();
            }
            counter++;
            users.next();
        }

        if (counter <= 1) return -1.0;
        return friendships / faculty(counter-1);
    }

    private double faculty(double n){
        if (n > 0){
            return n + faculty(n-1);
        }
        return 0;
    }

    /**
     * Gibt die möglichen Verbindungen zwischen zwei Personen im sozialen Netzwerk als String-Array zurück,
     * falls die Personen vorhanden sind und sie über eine oder mehrere Ecken miteinander verbunden sind.
     * @param name01
     * @param name02
     * @return
     */
    public String[] getLinksBetween(String name01, String name02){
        Vertex user01 = allUsers.getVertex(name01);
        Vertex user02 = allUsers.getVertex(name02);
        if(user01 != null && user02 != null){
            //TODO 13: Schreibe einen Algorithmus, der mindestens eine Verbindung von einem Nutzer über Zwischennutzer zu einem anderem Nutzer bestimmt. Happy Kopfzerbrechen!
            List<Vertex> path = new List<>();
            user01.setMark(true);
            path.append(user01);
            while (!path.isEmpty() && !user02.isMarked()){
                path.toLast();
                List<Vertex> neighbours = allUsers.getNeighbours(path.getContent());
                neighbours.toFirst();
                while (neighbours.hasAccess() && neighbours.getContent().isMarked()){
                    neighbours.next();
                }
                if (neighbours.hasAccess()){
                    path.append(neighbours.getContent());
                    neighbours.getContent().setMark(true);
                } else {
                    path.toLast();
                    path.remove();
                }
            }
            allUsers.setAllVertexMarks(false);
            if (!path.isEmpty()){
                int size = 0;
                for (path.toFirst(); path.hasAccess(); path.next()) size++;
                String[] output = new String[size];
                path.toFirst();
                for (int i = 0; i < size; i++){
                    output[i] = path.getContent().getID();
                    path.next();
                }
                return output;
            }
        }
        return null;
    }


    /**
     * Gibt zurück, ob es einen Knoten ohne Nachbarn gibt.
     * @return true, falls ein einersamer Knoten vorhanden ist, sonst false.
     */
    public boolean someoneIsLonely(){
        //TODO 14: Schreibe einen Algorithmus, der explizit den von uns benutzten Aufbau der Datenstruktur Graph und ihre angebotenen Methoden so ausnutzt, dass schnell (!) iterativ geprüft werden kann, ob der Graph allUsers keine einsamen Knoten hat. Dies lässt sich mit einer einzigen Schleife prüfen.
        List<Vertex> users = allUsers.getVertices();
        if (!users.isEmpty()){
            while(users.hasAccess()){
                List<Vertex> friends = allUsers.getNeighbours(users.getContent());
                if (friends.isEmpty()) return true;
                users.next();
            }
        }
        return false;
    }

    /**
     * Gibt zurück, ob vom ersten Knoten in der Liste aller Knoten ausgehend alle anderen Knoten erreicht also markiert werden können.
     * Nach der Prüfung werden noch vor der Rückgabe alle Knoten demarkiert.
     * @return true, falls alle Knoten vom ersten ausgehend markiert wurden, sonst false.
     */
    public boolean testIfConnected(){
        //TODO 15: Schreibe einen Algorithmus, der ausgehend vom ersten Knoten in der Liste aller Knoten versucht, alle anderen Knoten über Kanten zu erreichen und zu markieren.
        List<Vertex> users = allUsers.getVertices();
        if (!users.isEmpty()){
            allUsers.setAllVertexMarks(false);
            List<Vertex> connected = new List<>();
            connected.append(users.getContent());
            connected.toFirst();
            connected.getContent().setMark(true);
            while (connected.hasAccess()){
                List<Vertex> neighbours = allUsers.getNeighbours(connected.getContent());
                neighbours.toFirst();
                while (neighbours.hasAccess()){
                    if (!neighbours.getContent().isMarked()){
                        neighbours.getContent().setMark(true);
                        connected.append(neighbours.getContent());
                    }
                    neighbours.next();
                }
                connected.next();
            }

            if (allUsers.allVerticesMarked()){
                allUsers.setAllVertexMarks(false);
                return true;
            }
            allUsers.setAllVertexMarks(false);
        }
        return false;
    }
    
    /**
     * Gibt einen String-Array zu allen Knoten zurück, die von einem Knoten ausgehend erreichbar sind, falls die Person vorhanden ist.
     * Im Anschluss werden vor der Rückgabe alle Knoten demarkiert.
     * @return Alle erreichbaren Knoten als String-Array, sonst null.
     */
    public String[] transitiveFriendships(String name){
        //TODO 16: Schreibe einen Algorithmus, der ausgehend von einem Knoten alle erreichbaren ausgibt.
        Vertex v = allUsers.getVertex(name);
        if (v != null){
            allUsers.setAllVertexMarks(false);
            v.setMark(true);
            List<Vertex> access = allUsers.getNeighbours(v);
            access.toFirst();
            if (!access.isEmpty()){
                while (access.hasAccess()){
                    access.getContent().setMark(true);
                    access.next();
                }
                access.toFirst();
                int counter = 0;
                while (access.hasAccess()){
                    List<Vertex> neighbours = allUsers.getNeighbours(access.getContent());
                    neighbours.toFirst();
                    while (neighbours.hasAccess()){
                        if (!neighbours.getContent().isMarked()){
                            access.append(neighbours.getContent());
                            neighbours.getContent().setMark(true);
                        }
                        neighbours.next();
                    }
                    access.next();
                    counter++;
                }
                String[] array = new String[counter];
                access.toFirst();
                for (int i = 0; i < counter; i++){
                    array[i] = access.getContent().getID();
                    access.next();
                }
                return array;
            }
        }
        return null;
    }
    
    
    /**
     * Gibt eine kürzeste Verbindung zwischen zwei Personen des Sozialen Netzwerkes als String-Array zurück,
     * falls die Personen vorhanden sind und sie über eine oder mehrere Ecken miteinander verbunden sind.
     * @param name01
     * @param name02
     * @return Verbindung als String-Array oder null, falls es keine Verbindung gibt.
    */
    public String[] shortestPath(String name01, String name02){
        Vertex user01 = allUsers.getVertex(name01);
        Vertex user02 = allUsers.getVertex(name02);
        if(user01 != null && user02 != null){
            //TODO 17: Schreibe einen Algorithmus, der die kürzeste Verbindung zwischen den Nutzern name01 und name02 als String-Array zurückgibt. Versuche dabei einen möglichst effizienten Algorithmus zu schreiben.
        }
        return null;
    }

}
