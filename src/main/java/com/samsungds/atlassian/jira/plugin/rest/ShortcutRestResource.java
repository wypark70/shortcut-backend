package com.samsungds.atlassian.jira.plugin.rest;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.samsungds.atlassian.jira.plugin.ao.Shortcut;
import com.samsungds.atlassian.jira.plugin.domain.ShortcutVo;
import net.java.ao.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

@Path("/shortcut")
public class ShortcutRestResource {
    Logger log = LoggerFactory.getLogger(ShortcutRestResource.class);

    @ComponentImport
    private final ActiveObjects ao;

    @Inject
    public ShortcutRestResource(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    @GET
    @AnonymousAllowed
    @Path("/shortcuts/userName/{userName}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getShortcutsByUserName(@PathParam("userName") String userName) {
        List<Shortcut> shortcutList = newArrayList(ao.find(Shortcut.class, Query.select().where("user_name = ?", userName)));
        Iterator<Shortcut> shortcutIterator = shortcutList.iterator();
        List<ShortcutVo> shortcutVoList = new ArrayList<>();
        while (shortcutIterator.hasNext()) {
            Shortcut shortcut = shortcutIterator.next();
            ShortcutVo shortcutVo = new ShortcutVo();
            shortcutVo.setId(shortcut.getID());
            shortcutVo.setName(shortcut.getName());
            shortcutVo.setUrl(shortcut.getUrl());
            shortcutVo.setUserName(shortcut.getUserName());
            shortcutVoList.add(shortcutVo);
        }
        return Response.ok(shortcutVoList).build();
    }

    @POST
    @AnonymousAllowed
    @Path("/shortcuts/userName/{userName}/name/{name}/url/{url}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response addShortcut(@PathParam("userName") String userName, @PathParam("name") String name, @PathParam("url") String url) {
        final Shortcut shortcut = ao.create(Shortcut.class);
        shortcut.setName(name);
        shortcut.setUrl(url);
        shortcut.setUserName(userName);
        shortcut.save();
        return Response.ok("post").build();
    }

    @PUT
    @AnonymousAllowed
    @Path("/shortcuts/userName/{userName}/id/{id}/name/{name}/url/{url}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response modifyShortcut(@PathParam("userName") String userName, @PathParam("id") int id, @PathParam("name") String name, @PathParam("url") String url) {
        List<Shortcut> shortcutList = newArrayList(ao.find(Shortcut.class, Query.select().where("id = ?", id)));
        Shortcut shortcut = shortcutList.get(0);
        shortcut.setName(name);
        shortcut.setUrl(url);
        shortcut.setUserName(userName);
        shortcut.save();
        return Response.ok("put").build();
    }

    @DELETE
    @AnonymousAllowed
    @Path("/shortcuts/userName/{userName}/id/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response modifyShortcut(@PathParam("userName") String userName, @PathParam("id") int id) {
        List<Shortcut> shortcutList = newArrayList(ao.find(Shortcut.class, Query.select().where("id = ?", id)));
        Shortcut shortcut = shortcutList.get(0);
        ao.delete(shortcut);
        return Response.ok("delete").build();
    }

}