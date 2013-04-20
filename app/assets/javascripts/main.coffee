jQuery ->

  # Add click handlers to the links inside the given context.
  # @param $context JQuery element to look inside.
  addClickHandlers = ($context) ->
    # Add an empty dish - add action click handler.
    $('.add-dish', $context).click (event) ->
      $newDish = $('#dish_template').clone().removeAttr('id')
      $newDish.insertBefore($(event.target).parent()).hide().slideDown()
      renumber()
      addClickHandlers($newDish)

    # Delete a section - delete action click handler.
    $('.delete-section', $context).click (event) ->
      section = $(event.target).parent().parent()
      section.slideUp 'fast', ->
        section.remove()
        renumber()

    # Delete a dish - delete action click handler.
    $('.delete-dish', $context).click (event) ->
      dish = $(event.target).parent()
      dish.slideUp 'fast', ->
        dish.remove()
        renumber()


  # Add an empty dish - add action click handler.
  $('.add-section').click (event) ->
    $newSection = $('#section_template').clone().removeAttr('id')
    $newSection.insertBefore($(event.target).parent()).hide().slideDown()
    renumber()
    addClickHandlers($newSection)


  # Renumber the dish input name indices.
  renumber = ->
    $('form fieldset.section').each (sectionIndex) ->
      section = 'sections[' + sectionIndex + '].'
      console.log(section)
      $(@).find('p.section', this).each ->
        $(@).find('input').each ->
          # e.g. name="section[0].title"
          $(@).attr 'name', section + $(@).attr 'class'
      $(@).find('p.dish').each (dishIndex) ->
        dish = 'dishes[' + dishIndex + '].'
        console.log(section + dish)
        $(@).find('input').each ->
          # e.g. name="section[1].dish[2].price"
          $(@).attr 'name', section + dish + $(@).attr 'class'

  addClickHandlers()
