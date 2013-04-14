jQuery ->

  # Delete a dish - delete action click handler.
  addDeleteHandlers = ->
    $('.delete').click (event) ->
      dish = $(event.target).parent()
      dish.slideUp 'fast', ->
        dish.remove()
        renumber()

  # Add an empty dish - add action click handler.
  $('.add').click (event) ->
    $newDish = $('#dish_template').clone().removeAttr('id')
    $newDish.insertBefore($(event.target).parent()).hide().slideDown()
    renumber()
    addDeleteHandlers()

  # Renumber the dish input name indices.
  renumber = ->
    $('p.dish').each (index) ->
      $(@).find('input').each ->
        fieldName = $(this).attr 'name'
        $(this).attr 'name', fieldName.replace(/\d/, index)

  addDeleteHandlers()
