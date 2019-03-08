package com.packt.rest.todo;

import com.packt.rest.hateoas.HateoasLink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TodoDTO {

	private Long id;
	private String text;
	private List<HateoasLink> links;

	public TodoDTO(Todo todo) {
		this.id = todo.getId();
		this.text = todo.getText();
		this.links = Arrays.asList(
				new HateoasLink("self", "/todos/" + todo.getId()),
				new HateoasLink("tasks", "/todos/" + todo.getId() + "/tasks")
		);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<HateoasLink> getLinks() {
		return links;
	}

	public void setLinks(List<HateoasLink> links) {
		this.links = links;
	}
}
