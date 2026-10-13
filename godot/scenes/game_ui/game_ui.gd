extends CanvasLayer
class_name GameUI

const MEDAL_BRONZE = preload("res://Flappy Bird Assets/Sprites/UI/medalBronze.png")
const MEDAL_GOLD = preload("res://Flappy Bird Assets/Sprites/UI/medalGold.png")
const MEDAL_SILVER = preload("res://Flappy Bird Assets/Sprites/UI/medalSilver.png")

@onready var medal_img: TextureRect = %MedalImg
@onready var current_score: Label = %CurrentScore
@onready var highscore: Label = %Highscore

@onready var score_label: Label = %ScoreLabel
@onready var start_menu: Control = %StartMenu
@onready var game_over_menu: VBoxContainer = %GameOverMenu

func _ready() -> void:
	score_label.text = "0"

func update_score(value: int) -> void:
	score_label.text = str(value)

func calculate_score(score: int, high: int) -> void:
	current_score.text = str(score)
	highscore.text = str(high)
	if score >= 4:
		medal_img.texture = MEDAL_GOLD
	elif score >= 2:
		medal_img.texture = MEDAL_SILVER
	else:
		medal_img.texture = MEDAL_BRONZE

func game_over() -> void:
	game_over_menu.show()
	score_label.hide()

func _on_play_button_pressed() -> void:
	get_tree().reload_current_scene()
