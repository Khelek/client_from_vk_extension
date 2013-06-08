package client_side_java;

import android.util.Pair;
import client_side_java.VKResponseClasses.City;
import client_side_java.VKResponseClasses.Person;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.XMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: haukot
 * Date: 31.05.13
 * Time: 2:26
 * To change this template use File | Settings | File Templates.
 */
public class Algorithm {

    private Instances getInst(List<Person> ls) throws ParseException {
        ArrayList<Attribute> attrs = new ArrayList<Attribute>();
        attrs.add(new Attribute("uid", 0));
        attrs.add(new Attribute("sex", 1));
        attrs.add(new Attribute("bdate", "dd-MM-yyyy"));
        attrs.add(new Attribute("city", 3));
        attrs.add(new Attribute("country", 4));
        attrs.add(new Attribute("hasMobile", 5));
        attrs.add(new Attribute("relation", 6));

        Instances dataset = new Instances("users", attrs, 0);

        for (int i = 0; i < ls.size(); i++){
            dataset.add(getInstFromPacket(attrs, ls.get(i)));
        }

        return dataset;
    }

    private Instance getInstFromPacket(ArrayList<Attribute> attrs, Person pack) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat ("dd-MM-yyyy");
        SimpleDateFormat smallData = new SimpleDateFormat ("dd.MM");
        SimpleDateFormat longData = new SimpleDateFormat ("dd.MM.yyyy");
        Instance i = new DenseInstance(attrs.size());
        i.setValue(attrs.get(0), pack.uid);
        i.setValue(attrs.get(1), pack.sexId);
        if (pack.birthDate == null) {
            i.setValue(attrs.get(2), attrs.get(2).parseDate("01-01-1901"));
        } else {
            i.setValue(attrs.get(2), attrs.get(2).parseDate(
                    ft.format(pack.birthDate.length() < 6 ?
                            smallData.parse(pack.birthDate) :
                            longData.parse(pack.birthDate))
            ));
        }
        i.setValue(attrs.get(3), (pack.city == null) ? 0 : pack.city.cid);
        i.setValue(attrs.get(4), pack.country);
        i.setValue(attrs.get(5), pack.hasMobile);
        i.setValue(attrs.get(6), pack.relationId);
        return i;
    }

    private Pair<Integer, Instances> clusteringInstances(Instances data) throws Exception{
        String[] options = new String[18];
        options[0] = "-I";                 // max. iterations
        options[1] = "3";
        options[2] = "-M";
        options[3] = "1000";
        options[4] = "-J";
        options[5] = "1000";
        options[6] = "-L";                 //weka.clusterers.XMeans -I 1 -M 1000 -J 1000 -L 2 -H 4 -B 1.0 -C 0.5 -D "weka.core.EuclideanDistance -R first-last" -S 10
        options[7] = "2";
        options[8] = "-H";
        options[9] = "4";
        options[10] = "-B";
        options[11] = "1.0";
        options[12] = "-C";
        options[13] = "0.5";
        options[14] = "-D";
        options[15] = "weka.core.EuclideanDistance -R first-last";
        options[16] = "-S";
        options[17] = "10";

        XMeans clusterer = new XMeans();

        clusterer.setOptions(options);
        clusterer.buildClusterer(data);

        // evaluate clusterer
        ClusterEvaluation eval = new ClusterEvaluation();
        eval.setClusterer(clusterer);
        eval.evaluateClusterer(new Instances(data));

        AddCluster filter= new AddCluster();
        filter.setClusterer(clusterer);
        filter.setInputFormat(data);
        filter.useFilter(data,filter);
        Instances newInstances = Filter.useFilter(data, filter);

        for (int i=0; i<newInstances.numInstances();i++ )
        {
            Instance row = newInstances.instance(i);
            int attrCount = row.numAttributes();
            Attribute att = row.attribute(attrCount-1);
//NOTE --- if I run this I
//get - say - 1 record under cluster 0.
            //Doesn't match with (1)
        }
        return new Pair<Integer, Instances>(eval.getNumClusters(), newInstances);
    }

    private List<List<Person>> getClustersMembers(Pair<Integer, Instances> data, List<Person> allUsers){
        List<List<Person>> res = new ArrayList<List<Person>>(data.first);
        for (int i = 0; i < data.first; i++) {
            res.add(new ArrayList<Person>());
        }
        for (int i = 0; i < data.second.size(); i++) {
            String clusterName = data.second.get(i).stringValue(7);
            int clusterIndex = Integer.parseInt(clusterName.charAt(clusterName.length() - 1) + "");
            res.get(clusterIndex - 1).add(allUsers.get(i));
        }
        return res;
    }

    public List<List<Person>> clustering(List<Person> users){
        List<List<Person>> res = null;
           try{
               Instances data = getInst(users);
               Pair<Integer, Instances> newData = clusteringInstances(data);
               res = getClustersMembers(newData, users);
           } catch (Exception ex){
               ex.printStackTrace();
           }
        return res;
    }
}
