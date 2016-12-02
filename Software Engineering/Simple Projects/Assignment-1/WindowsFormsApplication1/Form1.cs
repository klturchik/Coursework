using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApplication1
{
    public partial class Form1 : Form
    {
        Double input = 0.0;                         //user input
        Double value = 0.0;                         //result of last operation
        Double memory = 0.0;                        //value in memory
        String operation = String.Empty;            //string storing operation
        String previousOperation = String.Empty;    //string storing operation
        bool operator_pressed = false;
        bool enter_pressed = false;
        bool memRecall_pressed = false;

        public Form1()
        {
            InitializeComponent();
        }

        private void number_Click(object sender, EventArgs e)
        {
            Button button = (Button)sender;

            if ((textBox.Text == "0") || (operator_pressed) || (memRecall_pressed))
            {
                textBox.Clear();
                operator_pressed = false;
                memRecall_pressed = false;
            }

            if (enter_pressed)
            {
                textBox.Clear();
                equation.Text = String.Empty;
                enter_pressed = false;
            }

            if (button.Text == ".")
            {
                if (!textBox.Text.Contains("."))
                {
                    if (textBox.Text == String.Empty)
                        textBox.Text = "0";
                    textBox.Text += button.Text;
                }
            }else
            textBox.Text += button.Text;
        }

        private void operator_Click(object sender, EventArgs e)
        {
            Button button = (Button)sender;

            if (enter_pressed)
            {
                equation.Text = String.Empty;
                enter_pressed = false;
            }

            if (operator_pressed)
            {
                char[] ops = {'+','-','*','/'};
                operation = button.Text;

                equation.Text.Trim();
                equation.Text = equation.Text.Remove(equation.Text.LastIndexOfAny(ops)) + operation + " ";
            }
            else
            {
                input = Double.Parse(textBox.Text);
                perform_Operation();
                operation = button.Text;
                equation.Text = equation.Text + input + " " + operation + " ";
            }

            operator_pressed = true;
        }

        private void enter_Click(object sender, EventArgs e)
        {
            if (enter_pressed)
            {
                operation = previousOperation;

                if (operation != String.Empty)
                {
                    value = Double.Parse(textBox.Text);
                    equation.Text = String.Empty;
                    equation.Text = value + " " + operation + " " + input + " =";
                }
                else 
                {
                    value = Double.Parse(textBox.Text);
                    equation.Text = value + " =";
                }
            }
            else
            {
                input = Double.Parse(textBox.Text);
                equation.Text = equation.Text + input + " =";
            }

            if (operation != String.Empty)
                perform_Operation();

            value = 0.0;
            previousOperation = operation;
            operation = String.Empty;

            operator_pressed = false;
            enter_pressed = true;
        }

        private void perform_Operation()
        {
            switch (operation)
            {
                case "+":
                    value += input;
                    textBox.Text = value.ToString();
                    break;
                case "-":
                    value -= input;
                    textBox.Text = value.ToString();
                    break;
                case "*":
                    value *= input;
                    textBox.Text = value.ToString();
                    break;
                case "/":
                    value /= input;
                    textBox.Text = value.ToString();
                    break;
                default:
                    value = input;
                    break;
            }
        }

        private void buttonBack_Click(object sender, EventArgs e)
        {
            if (!enter_pressed && !operator_pressed && textBox.Text != "0")
                textBox.Text = textBox.Text.Substring(0, textBox.Text.Length - 1);
            if (textBox.Text == String.Empty)
                textBox.Text = "0";
        }

        private void clearEntry_Click(object sender, EventArgs e)
        {
            textBox.Text = "0";
        }

        private void clear_Click(object sender, EventArgs e)
        {
            textBox.Text = "0";
            equation.Text = String.Empty;

            input = 0.0;
            value = 0.0;
            operation = String.Empty;
            operator_pressed = false;
            enter_pressed = false;
            memRecall_pressed = false;
        }

        private void buttonSign_Click(object sender, EventArgs e)
        {
            textBox.Text = (Double.Parse(textBox.Text) * (-1)).ToString();
        }

        private void buttonMemClear_Click(object sender, EventArgs e)
        {
            memory = 0.0;
        }

        private void buttonMemRecall_Click(object sender, EventArgs e)
        {
            textBox.Text = memory.ToString();
            memRecall_pressed = true;

            operator_pressed = false;
        }

        private void buttonMemStore_Click(object sender, EventArgs e)
        {
            memory = Double.Parse(textBox.Text);
        }
    }
}
