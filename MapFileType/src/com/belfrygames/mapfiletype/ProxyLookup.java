package com.belfrygames.mapfiletype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.LookupListener;

/**
 *
 * @author tomas
 */
public class ProxyLookup extends Lookup {

    private List<Lookup> lookups = new ArrayList<Lookup>();

    public void add(Lookup lkp) {
        lookups.add(lkp);
    }

    public void remove(Lookup lkp) {
        lookups.remove(lkp);
    }

    @Override
    public <T> T lookup(Class<T> clazz) {
        for (Lookup lkp : lookups) {
            T res = lkp.lookup(clazz);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public <T> Lookup.Result<T> lookup(final Lookup.Template<T> template) {
        return new Lookup.Result<T>() {

            @Override
            public void addLookupListener(LookupListener l) {
                for (Lookup lkp : lookups) {
                    Lookup.Result<T> res = lkp.lookup(template);
                    res.addLookupListener(l);
                }
            }

            @Override
            public void removeLookupListener(LookupListener l) {
                for (Lookup lkp : lookups) {
                    Lookup.Result<T> res = lkp.lookup(template);
                    res.removeLookupListener(l);
                }
            }

            @Override
            public Collection<? extends T> allInstances() {
                List instances = new ArrayList();
                for (Lookup lkp : lookups) {
                    Lookup.Result<T> res = lkp.lookup(template);
                    instances.addAll(res.allInstances());
                }
                return instances;
            }
        };
    }
}
