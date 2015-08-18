jQuery ($) ->

  $table = $('.container table')
  productListUrl = $table.data('list')

  loadProductTable = -> $.get productListUrl, (products) ->
    $.each products, (index, eanCode) ->
      row = $('<tr/>').append $('<td/>').text(eanCode)
      row.attr 'contenteditable', true #set content editable
      $table.append row
      loadProductDetails row

  #Construct product details url
  productDetailsUrl = (eanCode) ->
    $table.data('details').replace '0', eanCode

  loadProductDetails = (tableRow) ->
    #ean code from first column
    eanCode = tableRow.text()
    #fetch details for this ean
    $.get productDetailsUrl(eanCode), (product) ->
      tableRow.append $('<td/>').text(product.name)
      tableRow.append $('<td/>').text(product.description)

  loadProductTable()

  #Function to save row on server
  saveRow = ($row) ->
    [ean, name, description] = $row.children().map -> $(this).text()
    #Construct product javascript object
    product =
      #Send data to server
      ean: parseInt(ean)
      name: name
      description: description
    #Send data to server
    jqxhr = $.ajax
      type: "PUT"
      url: productDetailsUrl(ean)
      contentType: "application/json"
      data: JSON.stringify product #convert object to JSON string before sending
    #Show success message
    jqxhr.done (response) ->
      $label = $('<span/>').addClass('label label-success')
      $row.children().last().append $label.text(response)
      $label.delay(3000).fadeOut()
    #Show error message
    jqxhr.fail (data) ->
      $label = $('<span/>').addClass('label label-important')
      message = data.responseText || data.statusText
      $row.children().last().append $label.text(message)

  #Save changes when row loses focus
  $table.on 'focusout', 'tr', () ->
    saveRow $(this)
