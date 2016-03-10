package de.longor.talecraft.server;

import java.util.Map;

import com.google.common.collect.Maps;

import de.longor.talecraft.clipboard.ClipboardItem;

public class ServerClipboard {
	private Map<String, ClipboardItem> items;
	
	public ServerClipboard() {
		items = Maps.newHashMap();
	}
	
	public ClipboardItem get(String name) {
		return items.get(name);
	}
	
	public void put(String name, ClipboardItem item) {
		items.put(name, item);
	}
	
}
